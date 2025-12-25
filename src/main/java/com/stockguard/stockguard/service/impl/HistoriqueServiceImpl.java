package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.HistoriqueResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.HistoriqueMapper;
import com.stockguard.stockguard.model.Entrepot;
import com.stockguard.stockguard.model.HistoriqueVente;
import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.enums.JourSemaine;
import com.stockguard.stockguard.repository.EntrepotRepository;
import com.stockguard.stockguard.repository.HistoriqueRepository;
import com.stockguard.stockguard.repository.ProduitRepository;
import com.stockguard.stockguard.service.HistoriqueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoriqueServiceImpl implements HistoriqueService {

    private final HistoriqueRepository historiqueRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;
    private final HistoriqueMapper historiqueMapper;

    @Override
    @Transactional
    public HistoriqueResponse createHistorique(HistoriqueRequest request) {
        log.info("Création d'une nouvelle vente historique pour produit ID {} dans entrepôt ID {}",
                request.getProduitId(), request.getEntrepotId());

        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + request.getProduitId()));

        Entrepot entrepot = entrepotRepository.findById(request.getEntrepotId())
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + request.getEntrepotId()));

        HistoriqueVente historique = historiqueMapper.toEntity(request);
        historique.setProduit(produit);
        historique.setEntrepot(entrepot);

        HistoriqueVente savedHistorique = historiqueRepository.save(historique);

        log.info("Vente historique créée avec succès: ID {}", savedHistorique.getId());
        return historiqueMapper.toResponse(savedHistorique);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriqueResponse getHistoriqueById(Long id) {
        log.debug("Recherche de la vente historique avec ID: {}", id);

        HistoriqueVente historique = historiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente historique non trouvée avec ID: " + id));

        return historiqueMapper.toResponse(historique);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getAllHistoriques() {
        log.debug("Récupération de toutes les ventes historiques");

        return historiqueRepository.findAll().stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepot(Long entrepotId) {
        log.debug("Récupération des ventes historiques pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return historiqueRepository.findByEntrepotId(entrepotId).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByProduit(Long produitId) {
        log.debug("Récupération des ventes historiques pour le produit ID: {}", produitId);

        // Vérifier que le produit existe
        produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + produitId));

        return historiqueRepository.findByProduitId(produitId).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Récupération des ventes historiques entre {} et {}", startDate, endDate);

        return historiqueRepository.findByDateVenteBetween(startDate, endDate).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepotAndDateRange(Long entrepotId, LocalDate startDate, LocalDate endDate) {
        log.debug("Récupération des ventes historiques pour entrepôt {} entre {} et {}", entrepotId, startDate, endDate);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return historiqueRepository.findByEntrepotIdAndDateVenteBetween(entrepotId, startDate, endDate).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByJourSemaine(JourSemaine jourSemaine) {
        log.debug("Récupération des ventes historiques pour le jour: {}", jourSemaine);

        return historiqueRepository.findByJourSemaine(jourSemaine).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> getHistoriquesByEntrepotAndJourSemaine(Long entrepotId, JourSemaine jourSemaine) {
        log.debug("Récupération des ventes historiques pour entrepôt {} et jour {}", entrepotId, jourSemaine);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return historiqueRepository.findByEntrepotIdAndJourSemaine(entrepotId, jourSemaine).stream()
                .map(historiqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalVentesByProduitAndEntrepot(Long produitId, Long entrepotId) {
        log.debug("Calcul du total des ventes pour produit {} dans entrepôt {}", produitId, entrepotId);

        // Vérifier que le produit existe
        produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + produitId));

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        Long total = historiqueRepository.getTotalVentesByProduitAndEntrepot(produitId, entrepotId);
        return total != null ? total : 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVentesParJourSemaine(Long produitId, Long entrepotId) {
        log.debug("Récupération des ventes par jour de semaine pour produit {} dans entrepôt {}", produitId, entrepotId);

        return historiqueRepository.getVentesParJourSemaine(produitId, entrepotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVentesParMois(Long produitId, Long entrepotId, Integer annee) {
        log.debug("Récupération des ventes par mois pour produit {} dans entrepôt {} année {}", produitId, entrepotId, annee);

        return historiqueRepository.getVentesParMois(produitId, entrepotId, annee);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<JourSemaine, Long> getVentesTotalParJourSemaine(Long produitId, Long entrepotId) {
        log.debug("Calcul des ventes totales par jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        // Vérifier que le produit existe
        produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + produitId));

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        List<Object[]> resultats = historiqueRepository.getVentesParJourSemaine(produitId, entrepotId);

        // Initialiser avec tous les jours à zéro
        Map<JourSemaine, Long> ventesParJour = Arrays.stream(JourSemaine.values())
                .collect(Collectors.toMap(
                        jour -> jour,
                        jour -> 0L
                ));

        // Remplir avec les données réelles
        for (Object[] resultat : resultats) {
            JourSemaine jour = (JourSemaine) resultat[0];
            Long total = ((Number) resultat[1]).longValue();
            ventesParJour.put(jour, total);
        }

        // Trier par ordre des jours de la semaine
        return ventesParJour.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<JourSemaine, BigDecimal> getChiffreAffairesParJourSemaine(Long produitId, Long entrepotId) {
        log.debug("Calcul du chiffre d'affaires par jour de semaine pour produit {} et entrepôt {}", produitId, entrepotId);

        List<Object[]> resultats = historiqueRepository.getChiffreAffairesParJourSemaine(produitId, entrepotId);

        // Initialiser avec tous les jours à zéro
        Map<JourSemaine, BigDecimal> caParJour = Arrays.stream(JourSemaine.values())
                .collect(Collectors.toMap(
                        jour -> jour,
                        jour -> BigDecimal.ZERO
                ));

        // Remplir avec les données réelles
        for (Object[] resultat : resultats) {
            JourSemaine jour = (JourSemaine) resultat[0];
            BigDecimal ca = (BigDecimal) resultat[1];
            caParJour.put(jour, ca);
        }

        // Trier par ordre des jours de la semaine
        return caParJour.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public JourSemaine getMeilleurJourSemainePourProduit(Long produitId, Long entrepotId) {
        log.debug("Recherche du meilleur jour de vente pour produit {} dans entrepôt {}", produitId, entrepotId);

        List<Object[]> resultats = historiqueRepository.getMeilleurJourSemainePourProduit(produitId, entrepotId);

        if (resultats.isEmpty()) {
            return null;
        }

        return (JourSemaine) resultats.get(0)[0];
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesGlobalesParJourSemaine(Long entrepotId) {
        log.debug("Récupération des statistiques globales par jour de semaine pour entrepôt {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        List<Object[]> resultats = historiqueRepository.getStatistiquesGlobalesParJourSemaine(entrepotId);

        // Préparer les données
        List<String> joursLibelles = Arrays.stream(JourSemaine.values())
                .map(JourSemaine::getLibelle)
                .collect(Collectors.toList());

        Map<String, Long> ventesParJour = new LinkedHashMap<>();
        Map<String, BigDecimal> caParJour = new LinkedHashMap<>();

        // Initialiser avec zéros
        for (JourSemaine jour : JourSemaine.values()) {
            ventesParJour.put(jour.getLibelle(), 0L);
            caParJour.put(jour.getLibelle(), BigDecimal.ZERO);
        }

        // Remplir avec les données réelles
        for (Object[] resultat : resultats) {
            JourSemaine jour = (JourSemaine) resultat[0];
            Long ventes = ((Number) resultat[1]).longValue();
            BigDecimal ca = (BigDecimal) resultat[2];

            ventesParJour.put(jour.getLibelle(), ventes);
            caParJour.put(jour.getLibelle(), ca);
        }

        // Calculer les totaux
        long totalVentes = ventesParJour.values().stream().mapToLong(Long::longValue).sum();
        BigDecimal totalCA = caParJour.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        // Trouver le jour avec le plus de ventes
        String meilleurJour = ventesParJour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        // Trouver le jour avec le plus de CA
        String meilleurJourCA = caParJour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        // Construire la réponse
        Map<String, Object> statistiques = new LinkedHashMap<>();
        statistiques.put("entrepotId", entrepotId);
        statistiques.put("jours", joursLibelles);
        statistiques.put("ventesParJour", ventesParJour);
        statistiques.put("chiffreAffairesParJour", caParJour);
        statistiques.put("totalVentes", totalVentes);
        statistiques.put("totalChiffreAffaires", totalCA);
        statistiques.put("meilleurJourVentes", meilleurJour);
        statistiques.put("meilleurJourCA", meilleurJourCA);
        statistiques.put("moyenneVentesParJour", totalVentes / 7);
        statistiques.put("moyenneCAParJour", totalCA.divide(new BigDecimal("7"), 2, java.math.RoundingMode.HALF_UP));

        return statistiques;
    }

    // Méthodes supplémentaires pour les tableaux de bord

    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesVentesParEntrepot() {
        log.debug("Calcul des statistiques de ventes par entrepôt");

        List<Entrepot> entrepots = entrepotRepository.findByActifTrue();
        Map<String, Object> statistiques = new LinkedHashMap<>();

        for (Entrepot entrepot : entrepots) {
            // Calculer le CA total pour cet entrepôt
            BigDecimal caTotal = historiqueRepository.getChiffreAffairesByEntrepotAndPeriod(
                    entrepot.getId(),
                    LocalDate.now().minusMonths(1),
                    LocalDate.now()
            );

            if (caTotal == null) {
                caTotal = BigDecimal.ZERO;
            }

            // Nombre de ventes récentes
            List<HistoriqueVente> ventesRecent = historiqueRepository.findRecentByEntrepot(
                    entrepot.getId(),
                    LocalDate.now().minusDays(7)
            );

            // Produits les plus vendus
            Map<String, Long> topProduits = new LinkedHashMap<>();
            List<HistoriqueVente> ventesEntrepot = historiqueRepository.findByEntrepotId(entrepot.getId());

            ventesEntrepot.stream()
                    .collect(Collectors.groupingBy(
                            v -> v.getProduit().getNom(),
                            Collectors.summingLong(HistoriqueVente::getQuantiteVendue)
                    ))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .forEach(entry -> topProduits.put(entry.getKey(), entry.getValue()));

            statistiques.put(entrepot.getNom(), Map.of(
                    "id", entrepot.getId(),
                    "ville", entrepot.getVille(),
                    "chiffreAffairesMensuel", caTotal,
                    "nombreVentes7jours", ventesRecent.size(),
                    "topProduits", topProduits,
                    "tauxRemplissage", entrepot.getTauxRemplissage()
            ));
        }

        return statistiques;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesPeriodiques(LocalDate startDate, LocalDate endDate) {
        log.debug("Calcul des statistiques périodiques entre {} et {}", startDate, endDate);

        List<HistoriqueVente> ventes = historiqueRepository.findByDateVenteBetween(startDate, endDate);

        if (ventes.isEmpty()) {
            return Map.of("message", "Aucune vente trouvée pour cette période");
        }

        // Calculs globaux
        long totalVentes = ventes.stream().mapToLong(HistoriqueVente::getQuantiteVendue).sum();
        BigDecimal totalCA = ventes.stream()
                .map(HistoriqueVente::getChiffreAffaires)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Par entrepôt
        Map<String, Object> parEntrepot = new LinkedHashMap<>();
        ventes.stream()
                .collect(Collectors.groupingBy(v -> v.getEntrepot().getNom()))
                .forEach((entrepotNom, ventesEntrepot) -> {
                    long ventesCount = ventesEntrepot.stream().mapToLong(HistoriqueVente::getQuantiteVendue).sum();
                    BigDecimal caEntrepot = ventesEntrepot.stream()
                            .map(HistoriqueVente::getChiffreAffaires)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    parEntrepot.put(entrepotNom, Map.of(
                            "ventes", ventesCount,
                            "chiffreAffaires", caEntrepot,
                            "pourcentageVentes", (ventesCount * 100.0) / totalVentes,
                            "pourcentageCA", caEntrepot.multiply(new BigDecimal("100"))
                                    .divide(totalCA, 2, java.math.RoundingMode.HALF_UP)
                    ));
                });

        // Par produit
        Map<String, Object> parProduit = new LinkedHashMap<>();
        ventes.stream()
                .collect(Collectors.groupingBy(v -> v.getProduit().getNom()))
                .forEach((produitNom, ventesProduit) -> {
                    long ventesCount = ventesProduit.stream().mapToLong(HistoriqueVente::getQuantiteVendue).sum();
                    BigDecimal caProduit = ventesProduit.stream()
                            .map(HistoriqueVente::getChiffreAffaires)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    parProduit.put(produitNom, Map.of(
                            "ventes", ventesCount,
                            "chiffreAffaires", caProduit
                    ));
                });

        // Par jour de semaine
        Map<String, Long> ventesParJour = new LinkedHashMap<>();
        ventes.stream()
                .collect(Collectors.groupingBy(v -> v.getJourSemaine().getLibelle()))
                .forEach((jour, ventesJour) -> {
                    long total = ventesJour.stream().mapToLong(HistoriqueVente::getQuantiteVendue).sum();
                    ventesParJour.put(jour, total);
                });

        // Tendance jour par jour
        Map<LocalDate, Long> tendanceJournaliere = new LinkedHashMap<>();
        ventes.stream()
                .collect(Collectors.groupingBy(HistoriqueVente::getDateVente))
                .forEach((date, ventesDate) -> {
                    long total = ventesDate.stream().mapToLong(HistoriqueVente::getQuantiteVendue).sum();
                    tendanceJournaliere.put(date, total);
                });

        return Map.of(
                "periode", Map.of("debut", startDate, "fin", endDate),
                "totalVentes", totalVentes,
                "totalChiffreAffaires", totalCA,
                "ventesMoyennesParJour", totalVentes / (endDate.toEpochDay() - startDate.toEpochDay() + 1),
                "CAMoyenParJour", totalCA.divide(
                        new BigDecimal(String.valueOf(endDate.toEpochDay() - startDate.toEpochDay() + 1)),
                        2,
                        java.math.RoundingMode.HALF_UP
                ),
                "parEntrepot", parEntrepot,
                "parProduit", parProduit,
                "parJourSemaine", ventesParJour,
                "tendanceJournaliere", tendanceJournaliere,
                "nombreJoursAvecVentes", tendanceJournaliere.size(),
                "jourRecord", tendanceJournaliere.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(e -> Map.of("date", e.getKey(), "ventes", e.getValue()))
                        .orElse(null)
        );
    }
}