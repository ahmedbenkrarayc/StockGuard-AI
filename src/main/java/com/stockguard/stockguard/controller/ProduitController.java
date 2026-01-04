package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.model.enums.Unite;
import com.stockguard.stockguard.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> createProduit(@Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.createProduit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> getProduitById(@PathVariable Long id) {
        ProduitResponse response = produitService.getProduitById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getAllProduits() {
        List<ProduitResponse> response = produitService.getAllProduits();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actifs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getProduitsActifs() {
        List<ProduitResponse> response = produitService.getProduitsActifs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.updateProduit(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactiver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactiverProduit(@PathVariable Long id) {
        produitService.desactiverProduit(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activerProduit(@PathVariable Long id) {
        produitService.activerProduit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorie}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getProduitsByCategorie(@PathVariable String categorie) {
        List<ProduitResponse> response = produitService.getProduitsByCategorie(categorie);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> searchProduits(@RequestParam String keyword) {
        List<ProduitResponse> response = produitService.searchProduits(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unite/{unite}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getProduitsByUnite(@PathVariable Unite unite) {
        List<ProduitResponse> response = produitService.getProduitsByUnite(unite);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> response = produitService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countProduitsActifs() {
        long count = produitService.countProduitsActifs();
        return ResponseEntity.ok(count);
    }
}