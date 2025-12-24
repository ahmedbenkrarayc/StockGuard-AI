package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
import com.stockguard.stockguard.dto.response.EntrepotResponse;

import java.util.List;

public interface EntrepotService {

    EntrepotResponse createEntrepot(EntrepotRequest request);
    EntrepotResponse getEntrepotById(Long id);
    List<EntrepotResponse> getAllEntrepots();
    List<EntrepotResponse> getEntrepotsActifs();
    EntrepotResponse updateEntrepot(Long id, EntrepotRequest request);
    void deleteEntrepot(Long id);
    void desactiverEntrepot(Long id);
    void activerEntrepot(Long id);

    // Méthodes supplémentaires
    List<EntrepotResponse> getEntrepotsByVille(String ville);
    List<EntrepotResponse> getEntrepotsWithAvailableCapacity(Double volume);
    EntrepotResponse getEntrepotByNom(String nom);
    Double calculerTauxRemplissage(Long entrepotId);
    long countEntrepotsActifs();
    Double getAverageTauxRemplissage();
}