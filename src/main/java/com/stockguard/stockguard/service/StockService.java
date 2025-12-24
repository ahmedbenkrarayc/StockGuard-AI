package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.request.StockRequest;
import com.stockguard.stockguard.dto.request.UpdateStockRequest;
import com.stockguard.stockguard.dto.response.StockResponse;

import java.math.BigDecimal;
import java.util.List;

public interface StockService {

    StockResponse createStock(StockRequest request);
    StockResponse getStockById(Long id);
    List<StockResponse> getAllStocks();
    List<StockResponse> getStocksByEntrepot(Long entrepotId);
    List<StockResponse> getStocksByProduit(Long produitId);
    StockResponse updateStock(Long id, StockRequest request);
    void deleteStock(Long id);
    StockResponse ajouterQuantite(UpdateStockRequest request);
    StockResponse retirerQuantite(UpdateStockRequest request);

    // Méthodes d'alerte
    List<StockResponse> getStocksFaiblesByEntrepot(Long entrepotId);
    List<StockResponse> getAllStocksFaibles();
    List<StockResponse> getStocksEnRuptureByEntrepot(Long entrepotId);
    List<StockResponse> getSurStocks();

    // Méthodes utilitaires
    StockResponse getStockByProduitAndEntrepot(Long produitId, Long entrepotId);
    BigDecimal calculerValeurStockByEntrepot(Long entrepotId);
}