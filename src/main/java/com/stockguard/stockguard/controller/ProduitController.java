package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.model.enums.Unite;
import com.stockguard.stockguard.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitResponse> createProduit(@Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.createProduit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponse> getProduitById(@PathVariable Long id) {
        ProduitResponse response = produitService.getProduitById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProduitResponse>> getAllProduits() {
        List<ProduitResponse> response = produitService.getAllProduits();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<ProduitResponse>> getProduitsActifs() {
        List<ProduitResponse> response = produitService.getProduitsActifs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponse> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.updateProduit(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverProduit(@PathVariable Long id) {
        produitService.desactiverProduit(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerProduit(@PathVariable Long id) {
        produitService.activerProduit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<ProduitResponse>> getProduitsByCategorie(@PathVariable String categorie) {
        List<ProduitResponse> response = produitService.getProduitsByCategorie(categorie);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<ProduitResponse>> searchProduits(@RequestParam String keyword) {
        List<ProduitResponse> response = produitService.searchProduits(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unite/{unite}")
    public ResponseEntity<List<ProduitResponse>> getProduitsByUnite(@PathVariable Unite unite) {
        List<ProduitResponse> response = produitService.getProduitsByUnite(unite);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> response = produitService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> countProduitsActifs() {
        long count = produitService.countProduitsActifs();
        return ResponseEntity.ok(count);
    }
}