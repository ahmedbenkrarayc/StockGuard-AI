package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.StockRequest;
import com.stockguard.stockguard.dto.request.UpdateStockRequest;
import com.stockguard.stockguard.dto.response.StockResponse;
import com.stockguard.stockguard.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> createStock(@Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long id) {
        StockResponse response = stockService.getStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        List<StockResponse> response = stockService.getAllStocks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entrepot/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getStocksByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksByEntrepot(entrepotId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/produit/{produitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getStocksByProduit(@PathVariable Long produitId) {
        List<StockResponse> response = stockService.getStocksByProduit(produitId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/produit/{produitId}/entrepot/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> getStockByProduitAndEntrepot(
            @PathVariable Long produitId,
            @PathVariable Long entrepotId) {
        StockResponse response = stockService.getStockByProduitAndEntrepot(produitId, entrepotId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        StockResponse response = stockService.updateStock(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ajouter-quantite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> ajouterQuantite(@Valid @RequestBody UpdateStockRequest request) {
        StockResponse response = stockService.ajouterQuantite(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/retirer-quantite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockResponse> retirerQuantite(@Valid @RequestBody UpdateStockRequest request) {
        StockResponse response = stockService.retirerQuantite(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alertes/faibles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getAllStocksFaibles() {
        List<StockResponse> response = stockService.getAllStocksFaibles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alertes/faibles/entrepot/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getStocksFaiblesByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksFaiblesByEntrepot(entrepotId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alertes/rupture/entrepot/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getStocksEnRuptureByEntrepot(@PathVariable Long entrepotId) {
        List<StockResponse> response = stockService.getStocksEnRuptureByEntrepot(entrepotId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alertes/surstock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponse>> getSurStocks() {
        List<StockResponse> response = stockService.getSurStocks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entrepot/{entrepotId}/valeur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> calculerValeurStock(@PathVariable Long entrepotId) {
        BigDecimal valeur = stockService.calculerValeurStockByEntrepot(entrepotId);
        return ResponseEntity.ok(valeur);
    }
}