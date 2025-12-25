package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.HistoriqueResponse;
import com.stockguard.stockguard.model.enums.JourSemaine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HistoriqueService {

    HistoriqueResponse createHistorique(HistoriqueRequest request);
    HistoriqueResponse getHistoriqueById(Long id);
    List<HistoriqueResponse> getAllHistoriques();
    List<HistoriqueResponse> getHistoriquesByEntrepot(Long entrepotId);
    List<HistoriqueResponse> getHistoriquesByProduit(Long produitId);
    List<HistoriqueResponse> getHistoriquesByDateRange(LocalDate startDate, LocalDate endDate);
    List<HistoriqueResponse> getHistoriquesByEntrepotAndDateRange(Long entrepotId, LocalDate startDate, LocalDate endDate);

    // Nouvelles méthodes pour les jours de semaine
    List<HistoriqueResponse> getHistoriquesByJourSemaine(JourSemaine jourSemaine);
    List<HistoriqueResponse> getHistoriquesByEntrepotAndJourSemaine(Long entrepotId, JourSemaine jourSemaine);

    // Méthodes statistiques
    Long getTotalVentesByProduitAndEntrepot(Long produitId, Long entrepotId);
    List<Object[]> getVentesParJourSemaine(Long produitId, Long entrepotId);
    List<Object[]> getVentesParMois(Long produitId, Long entrepotId, Integer annee);

    // Nouvelles méthodes statistiques avancées
    Map<JourSemaine, Long> getVentesTotalParJourSemaine(Long produitId, Long entrepotId);
    Map<JourSemaine, BigDecimal> getChiffreAffairesParJourSemaine(Long produitId, Long entrepotId);
    JourSemaine getMeilleurJourSemainePourProduit(Long produitId, Long entrepotId);
    Map<String, Object> getStatistiquesGlobalesParJourSemaine(Long entrepotId);
}