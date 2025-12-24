package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.StockRequest;
import com.stockguard.stockguard.dto.request.UpdateStockRequest;
import com.stockguard.stockguard.dto.response.ApiResponse;
import com.stockguard.stockguard.dto.response.StockResponse;
import com.stockguard.stockguard.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<ApiResponse<StockResponse>> createStock(@Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Stock créé avec succès", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockResponse>> getStockById(@PathVariable Long id) {
        StockResponse response = stockService.getStockById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockResponse>>> getAllStocks() {
        List<StockResponse> response = stockService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getStocksByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getStocksByProduit(@PathVariable Long produitId) {
        List<StockResponse> response = stockService.getStocksByProduit(produitId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/produit/{produitId}/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<StockResponse>> getStockByProduitAndEntrepot(
            @PathVariable Long produitId,
            @PathVariable Long entrepotId) {
        StockResponse response = stockService.getStockByProduitAndEntrepot(produitId, entrepotId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.updateStock(id, request);
        return ResponseEntity.ok(ApiResponse.success("Stock mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok(ApiResponse.success("Stock supprimé avec succès", null));
    }

    @PostMapping("/ajouter-quantite")
    public ResponseEntity<ApiResponse<StockResponse>> ajouterQuantite(@Valid @RequestBody UpdateStockRequest request) {
        StockResponse response = stockService.ajouterQuantite(request);
        return ResponseEntity.ok(ApiResponse.success("Quantité ajoutée avec succès", response));
    }

    @PostMapping("/retirer-quantite")
    public ResponseEntity<ApiResponse<StockResponse>> retirerQuantite(@Valid @RequestBody UpdateStockRequest request) {
        StockResponse response = stockService.retirerQuantite(request);
        return ResponseEntity.ok(ApiResponse.success("Quantité retirée avec succès", response));
    }

    @GetMapping("/alertes/faibles")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getAllStocksFaibles() {
        List<StockResponse> response = stockService.getAllStocksFaibles();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/alertes/faibles/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getStocksFaiblesByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksFaiblesByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/alertes/rupture/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getStocksEnRuptureByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksEnRuptureByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/alertes/surstock")
    public ResponseEntity<ApiResponse<List<StockResponse>>> getSurStocks() {
        List<StockResponse> response = stockService.getSurStocks();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/entrepot/{entrepotId}/valeur")
    public ResponseEntity<ApiResponse<BigDecimal>> calculerValeurStock(@PathVariable Long entrepotId) {
        BigDecimal valeur = stockService.calculerValeurStockByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success("Valeur du stock", valeur));
    }
}