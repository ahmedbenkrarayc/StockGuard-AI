package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
import com.stockguard.stockguard.dto.response.ApiResponse;
import com.stockguard.stockguard.dto.response.EntrepotResponse;
import com.stockguard.stockguard.service.EntrepotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrepots")
@RequiredArgsConstructor
public class EntrepotController {

    private final EntrepotService entrepotService;

    @PostMapping
    public ResponseEntity<ApiResponse<EntrepotResponse>> createEntrepot(@Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.createEntrepot(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Entrepôt créé avec succès", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntrepotResponse>> getEntrepotById(@PathVariable Long id) {
        EntrepotResponse response = entrepotService.getEntrepotById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntrepotResponse>>> getAllEntrepots() {
        List<EntrepotResponse> response = entrepotService.getAllEntrepots();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/actifs")
    public ResponseEntity<ApiResponse<List<EntrepotResponse>>> getEntrepotsActifs() {
        List<EntrepotResponse> response = entrepotService.getEntrepotsActifs();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntrepotResponse>> updateEntrepot(
            @PathVariable Long id,
            @Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.updateEntrepot(id, request);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt supprimé avec succès", null));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<ApiResponse<Void>> desactiverEntrepot(@PathVariable Long id) {
        entrepotService.desactiverEntrepot(id);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt désactivé avec succès", null));
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<ApiResponse<Void>> activerEntrepot(@PathVariable Long id) {
        entrepotService.activerEntrepot(id);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt activé avec succès", null));
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<ApiResponse<List<EntrepotResponse>>> getEntrepotsByVille(@PathVariable String ville) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsByVille(ville);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<EntrepotResponse>> getEntrepotByNom(@PathVariable String nom) {
        EntrepotResponse response = entrepotService.getEntrepotByNom(nom);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/capacite/disponible")
    public ResponseEntity<ApiResponse<List<EntrepotResponse>>> getEntrepotsWithAvailableCapacity(
            @RequestParam Double volume) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsWithAvailableCapacity(volume);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/taux-remplissage")
    public ResponseEntity<ApiResponse<Double>> calculerTauxRemplissage(@PathVariable Long id) {
        Double taux = entrepotService.calculerTauxRemplissage(id);
        return ResponseEntity.ok(ApiResponse.success("Taux de remplissage", taux));
    }

    @GetMapping("/stats/count")
    public ResponseEntity<ApiResponse<Long>> countEntrepotsActifs() {
        long count = entrepotService.countEntrepotsActifs();
        return ResponseEntity.ok(ApiResponse.success("Nombre d'entrepôts actifs", count));
    }

    @GetMapping("/stats/taux-moyen")
    public ResponseEntity<ApiResponse<Double>> getAverageTauxRemplissage() {
        Double tauxMoyen = entrepotService.getAverageTauxRemplissage();
        return ResponseEntity.ok(ApiResponse.success("Taux de remplissage moyen", tauxMoyen));
    }
}