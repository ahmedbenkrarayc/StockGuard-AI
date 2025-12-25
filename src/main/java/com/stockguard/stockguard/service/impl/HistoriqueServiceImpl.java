package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.HistoriqueResponse;
import com.stockguard.stockguard.model.Entrepot;
import com.stockguard.stockguard.model.HistoriqueVente;
import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.enums.JourSemaine;
import com.stockguard.stockguard.repository.EntrepotRepository;
import com.stockguard.stockguard.repository.HistoriqueRepository;
import com.stockguard.stockguard.repository.ProduitRepository;
import com.stockguard.stockguard.service.HistoriqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoriqueServiceImpl implements HistoriqueService {

    private final HistoriqueRepository historiqueRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;

    @Override
    @Transactional
    public HistoriqueResponse createHistorique(HistoriqueRequest request) {
        log.info("Création d'un nouvel historique de vente");

        // Vérifier que le produit existe
        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé avec ID: " + request.getProduitId()));

        // Vérifier que l'entrepôt existe
        Entrepot entrepot = entrepotRepository.findById(request.getEntrepotId())
                .orElseThrow(() -> new EntityNotFoundException("Entrepôt non trouvé avec ID: " + request.getEntrepotId()));

        // Créer l'historique de vente
        HistoriqueVente historique = new HistoriqueVente();
        historique.setProduit(produit);
        historique.setEntrepot(entrepot);
        historique.setQuantiteVendue(request.getQuantiteVendue());
        historique.setPrixVente(request.getPrixVente());

        // Si dateVente est null, elle sera définie par @PrePersist
        if (request.getDateVente() != null) {
            historique.setDateVente(request.getDateVente());
        }

        // Sauvegarder
        HistoriqueVente savedHistorique = historiqueRepository.save(historique);
        log.info("Historique de vente créé avec ID: {}", savedHistorique.getId());

        return mapToResponse(savedHistorique);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriqueResponse getHistoriqueById(Long id) {
        log.info("Récupération de l'historique avec ID: {}", id);

        HistoriqueVente historique = historiqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historique non trouvé avec ID: " + id));

        return mapToResponse(historique);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getAllHistoriques() {
        log.info("Récupération de tous les historiques");

        List<HistoriqueVente> historiques = historiqueRepository.findAll();

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepot(Long entrepotId) {
        log.info("Récupération des historiques pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new EntityNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        List<HistoriqueVente> historiques = historiqueRepository.findByEntrepotId(entrepotId);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByProduit(Long produitId) {
        log.info("Récupération des historiques pour le produit ID: {}", produitId);

        // Vérifier que le produit existe
        produitRepository.findById(produitId)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé avec ID: " + produitId));

        List<HistoriqueVente> historiques = historiqueRepository.findByProduitId(produitId);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Récupération des historiques entre {} et {}", startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        List<HistoriqueVente> historiques = historiqueRepository.findByDateVenteBetween(startDate, endDate);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepotAndDateRange(Long entrepotId, LocalDate startDate, LocalDate endDate) {
        log.info("Récupération des historiques pour l'entrepôt {} entre {} et {}", entrepotId, startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        List<HistoriqueVente> historiques = historiqueRepository.findByEntrepotIdAndDateVenteBetween(entrepotId, startDate, endDate);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByJourSemaine(JourSemaine jourSemaine) {
        log.info("Récupération des historiques pour le jour de semaine: {}", jourSemaine);

        List<HistoriqueVente> historiques = historiqueRepository.findByJourSemaine(jourSemaine);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepotAndJourSemaine(Long entrepotId, JourSemaine jourSemaine) {
        log.info("Récupération des historiques pour l'entrepôt {} et jour de semaine: {}", entrepotId, jourSemaine);

        List<HistoriqueVente> historiques = historiqueRepository.findByEntrepotIdAndJourSemaine(entrepotId, jourSemaine);

        return historiques.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalVentesByProduitAndEntrepot(Long produitId, Long entrepotId) {
        log.info("Calcul du total des ventes pour produit {} et entrepôt {}", produitId, entrepotId);

        Long total = historiqueRepository.getTotalVentesByProduitAndEntrepot(produitId, entrepotId);
        return total != null ? total : 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVentesParJourSemaine(Long produitId, Long entrepotId) {
        log.info("Récupération des ventes par jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        return historiqueRepository.getVentesParJourSemaine(produitId, entrepotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVentesParMois(Long produitId, Long entrepotId, Integer annee) {
        log.info("Récupération des ventes par mois pour produit {}, entrepôt {} et année {}", produitId, entrepotId, annee);

        return historiqueRepository.getVentesParMois(produitId, entrepotId, annee);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<JourSemaine, Long> getVentesTotalParJourSemaine(Long produitId, Long entrepotId) {
        log.info("Récupération des ventes totales par jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        List<Object[]> results = historiqueRepository.getVentesParJourSemaine(produitId, entrepotId);

        return results.stream()
                .collect(Collectors.toMap(
                        result -> (JourSemaine) result[0],
                        result -> ((Number) result[1]).longValue()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<JourSemaine, BigDecimal> getChiffreAffairesParJourSemaine(Long produitId, Long entrepotId) {
        log.info("Récupération du chiffre d'affaires par jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        List<Object[]> results = historiqueRepository.getChiffreAffairesParJourSemaine(produitId, entrepotId);

        return results.stream()
                .collect(Collectors.toMap(
                        result -> (JourSemaine) result[0],
                        result -> (BigDecimal) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public JourSemaine getMeilleurJourSemainePourProduit(Long produitId, Long entrepotId) {
        log.info("Récupération du meilleur jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        List<Object[]> results = historiqueRepository.getMeilleurJourSemainePourProduit(produitId, entrepotId);

        if (results.isEmpty() || results.get(0)[0] == null) {
            return null;
        }

        return (JourSemaine) results.get(0)[0];
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesGlobalesParJourSemaine(Long entrepotId) {
        log.info("Récupération des statistiques globales par jour de semaine pour entrepôt {}", entrepotId);

        List<Object[]> results = historiqueRepository.getStatistiquesGlobalesParJourSemaine(entrepotId);

        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((JourSemaine) result[0]).name(),
                        result -> Map.of(
                                "jourSemaine", ((JourSemaine) result[0]).getLibelle(),
                                "totalVentes", ((Number) result[1]).longValue(),
                                "totalChiffreAffaires", result[2]
                        )
                ));
    }

    private HistoriqueResponse mapToResponse(HistoriqueVente historique) {
        HistoriqueResponse response = new HistoriqueResponse();
        response.setId(historique.getId());

        if (historique.getProduit() != null) {
            response.setProduitId(historique.getProduit().getId());
            response.setProduitNom(historique.getProduit().getNom());
        }

        if (historique.getEntrepot() != null) {
            response.setEntrepotId(historique.getEntrepot().getId());
            response.setEntrepotNom(historique.getEntrepot().getNom());
        }

        response.setDateVente(historique.getDateVente());
        response.setQuantiteVendue(historique.getQuantiteVendue());
        response.setPrixVente(historique.getPrixVente());
        response.setChiffreAffaires(historique.getChiffreAffaires());
        response.setJourSemaine(historique.getJourSemaine());
        response.setMois(historique.getMois());
        response.setAnnee(historique.getAnnee());
        response.setCreatedAt(historique.getCreatedAt());

        return response;
    }
}