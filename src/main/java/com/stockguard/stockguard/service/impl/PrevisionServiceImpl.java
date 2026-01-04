package com.stockguard.stockguard.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockguard.stockguard.dto.response.AIResponse;
import com.stockguard.stockguard.dto.request.AIRequest;
import com.stockguard.stockguard.dto.response.PrevisionResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.PrevisionMapper;
import com.stockguard.stockguard.model.*;
import com.stockguard.stockguard.repository.*;
import com.stockguard.stockguard.service.PrevisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrevisionServiceImpl implements PrevisionService {

    private final PrevisionRepository previsionRepository;
    private final HistoriqueRepository historiqueRepository;
    private final StockRepository stockRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;
    private final PrevisionMapper previsionMapper;

    // ObjectMapper configuré pour parser le JSON manuellement
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // Client REST
    private final RestClient restClient = RestClient.create();

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Override
    @Transactional
    public PrevisionResponse genererPrevision(Long produitId, Long entrepotId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));
        Entrepot entrepot = entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé"));
        Stock stock = stockRepository.findByProduitAndEntrepot(produit, entrepot)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé"));

        List<HistoriqueVente> historique = historiqueRepository
                .findByProduitIdAndEntrepotIdAndDateVenteBetween(
                        produitId, entrepotId, LocalDate.now().minusDays(60), LocalDate.now());

        String historiqueText = historique.stream()
                .map(h -> h.getDateVente() + ": " + h.getQuantiteVendue())
                .collect(Collectors.joining(", "));

        String prompt = String.format("""
            Tu es un expert logistique. Réponds UNIQUEMENT en JSON valide (pas de markdown, pas de texte avant/après).
            Produit: %s
            Stock: %d
            Ventes passées (60j): [%s]
            
            Structure JSON exigée:
            { "prediction": 0, "confiance": 0, "recommandation": "texte" }
            """, produit.getNom(), stock.getQuantiteDisponible(), historiqueText);

        Prevision prevision = new Prevision();
        prevision.setProduit(produit);
        prevision.setEntrepot(entrepot);
        prevision.setDatePrevision(LocalDate.now());

        try {
            log.info("Envoi requête à OpenRouter/DeepSeek...");

            // 1. Récupérer le JSON brut (String) pour déboguer
            String rawJson = restClient.post()
                    .uri(baseUrl + "/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("HTTP-Referer", "http://localhost:8080") // Requis par OpenRouter parfois
                    .header("X-Title", "StockGuard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(AIRequest.create(model, prompt))
                    .retrieve()
                    .body(String.class);

            log.info("Réponse brute reçue : {}", rawJson); // <--- REGARDEZ VOS LOGS ICI

            if (rawJson != null) {
                // 2. Parser la réponse OpenAI
                JsonNode apiRoot = objectMapper.readTree(rawJson);

                // Vérifier si erreur dans le body (même si status 200)
                if (apiRoot.has("error")) {
                    throw new RuntimeException("API Error: " + apiRoot.get("error").toPrettyString());
                }

                // Extraire le contenu du message
                if (apiRoot.has("choices") && apiRoot.get("choices").size() > 0) {
                    String content = apiRoot.get("choices").get(0).get("message").get("content").asText();

                    // Nettoyer le JSON (Markdown éventuel)
                    String cleanContent = content.replace("```json", "").replace("```", "").trim();
                    log.info("Contenu IA extrait : {}", cleanContent);

                    // 3. Parser la prévision
                    JsonNode predictionNode = objectMapper.readTree(cleanContent);
                    prevision.setQuantitePrevue30Jours(predictionNode.path("prediction").asInt(0));
                    prevision.setNiveauConfiance(predictionNode.path("confiance").asInt(0));
                    prevision.setRecommandation(predictionNode.path("recommandation").asText("Aucune recommandation"));
                } else {
                    throw new RuntimeException("Structure réponse inattendue (pas de 'choices')");
                }
            }
        } catch (Exception e) {
            log.error("ECHEC IA: {}", e.getMessage());
            // Fallback pour éviter les NULL
            prevision.setQuantitePrevue30Jours(0);
            prevision.setNiveauConfiance(0);
            prevision.setRecommandation("Erreur IA: " + e.getMessage() + ". Vérifiez la clé API.");
        }

        return previsionMapper.toResponse(previsionRepository.save(prevision));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrevisionResponse> getPrevisionsByEntrepot(Long entrepotId) {
        return previsionRepository.findByEntrepotIdOrderByDatePrevisionDesc(entrepotId).stream()
                .map(previsionMapper::toResponse)
                .collect(Collectors.toList());
    }

}