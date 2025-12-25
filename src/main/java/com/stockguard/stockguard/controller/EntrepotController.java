package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
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
    public ResponseEntity<EntrepotResponse> createEntrepot(@Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.createEntrepot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrepotResponse> getEntrepotById(@PathVariable Long id) {
        EntrepotResponse response = entrepotService.getEntrepotById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EntrepotResponse>> getAllEntrepots() {
        List<EntrepotResponse> response = entrepotService.getAllEntrepots();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsActifs() {
        List<EntrepotResponse> response = entrepotService.getEntrepotsActifs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrepotResponse> updateEntrepot(
            @PathVariable Long id,
            @Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.updateEntrepot(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverEntrepot(@PathVariable Long id) {
        entrepotService.desactiverEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerEntrepot(@PathVariable Long id) {
        entrepotService.activerEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsByVille(@PathVariable String ville) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsByVille(ville);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<EntrepotResponse> getEntrepotByNom(@PathVariable String nom) {
        EntrepotResponse response = entrepotService.getEntrepotByNom(nom);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacite/disponible")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsWithAvailableCapacity(
            @RequestParam Double volume) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsWithAvailableCapacity(volume);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/taux-remplissage")
    public ResponseEntity<Double> calculerTauxRemplissage(@PathVariable Long id) {
        Double taux = entrepotService.calculerTauxRemplissage(id);
        return ResponseEntity.ok(taux);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> countEntrepotsActifs() {
        long count = entrepotService.countEntrepotsActifs();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/taux-moyen")
    public ResponseEntity<Double> getAverageTauxRemplissage() {
        Double tauxMoyen = entrepotService.getAverageTauxRemplissage();
        return ResponseEntity.ok(tauxMoyen);
    }
}