package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<ProduitResponse>> createProduit(@Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.createProduit(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Produit créé avec succès", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitResponse>> getProduitById(@PathVariable Long id) {
        ProduitResponse response = produitService.getProduitById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> getAllProduits() {
        List<ProduitResponse> response = produitService.getAllProduits();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/actifs")
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> getProduitsActifs() {
        List<ProduitResponse> response = produitService.getProduitsActifs();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitResponse>> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.updateProduit(id, request);
        return ResponseEntity.ok(ApiResponse.success("Produit mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.ok(ApiResponse.success("Produit supprimé avec succès", null));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<ApiResponse<Void>> desactiverProduit(@PathVariable Long id) {
        produitService.desactiverProduit(id);
        return ResponseEntity.ok(ApiResponse.success("Produit désactivé avec succès", null));
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<ApiResponse<Void>> activerProduit(@PathVariable Long id) {
        produitService.activerProduit(id);
        return ResponseEntity.ok(ApiResponse.success("Produit activé avec succès", null));
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> getProduitsByCategorie(@PathVariable String categorie) {
        List<ProduitResponse> response = produitService.getProduitsByCategorie(categorie);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/recherche")
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> searchProduits(@RequestParam String keyword) {
        List<ProduitResponse> response = produitService.searchProduits(keyword);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/unite/{unite}")
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> getProduitsByUnite(@PathVariable Unite unite) {
        List<ProduitResponse> response = produitService.getProduitsByUnite(unite);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        List<String> response = produitService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/stats/count")
    public ResponseEntity<ApiResponse<Long>> countProduitsActifs() {
        long count = produitService.countProduitsActifs();
        return ResponseEntity.ok(ApiResponse.success("Nombre de produits actifs", count));
    }
}