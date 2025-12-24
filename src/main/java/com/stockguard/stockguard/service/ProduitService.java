package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.model.enums.Unite;

import java.util.List;

public interface ProduitService {

    ProduitResponse createProduit(ProduitRequest request);
    ProduitResponse getProduitById(Long id);
    List<ProduitResponse> getAllProduits();
    List<ProduitResponse> getProduitsActifs();
    ProduitResponse updateProduit(Long id, ProduitRequest request);
    void deleteProduit(Long id);
    void desactiverProduit(Long id);
    void activerProduit(Long id);

    // Méthodes supplémentaires
    List<ProduitResponse> getProduitsByCategorie(String categorie);
    List<ProduitResponse> searchProduits(String keyword);
    List<ProduitResponse> getProduitsByUnite(Unite unite);
    List<String> getAllCategories();
    long countProduitsActifs();
}