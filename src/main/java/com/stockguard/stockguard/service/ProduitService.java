package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;

import java.util.List;

public interface ProduitService {
    ProduitResponse createProduit(ProduitRequest request);
    ProduitResponse getProduitById(Long id, String role);
    List<ProduitResponse> getAllProduits(String role);
    List<ProduitResponse> getProduitsActifs(String role);
    ProduitResponse updateProduit(Long id, ProduitRequest request, String role);
    void deleteProduit(Long id);
    void desactiverProduit(Long id);
    void activerProduit(Long id);
}
