package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
import com.stockguard.stockguard.dto.response.EntrepotResponse;
import com.stockguard.stockguard.service.EntrepotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrepots")
@RequiredArgsConstructor
public class EntrepotController {

    private final EntrepotService entrepotService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrepotResponse> createEntrepot(@Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.createEntrepot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrepotResponse> getEntrepotById(@PathVariable Long id) {
        EntrepotResponse response = entrepotService.getEntrepotById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntrepotResponse>> getAllEntrepots() {
        List<EntrepotResponse> response = entrepotService.getAllEntrepots();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actifs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsActifs() {
        List<EntrepotResponse> response = entrepotService.getEntrepotsActifs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrepotResponse> updateEntrepot(
            @PathVariable Long id,
            @Valid @RequestBody EntrepotRequest request) {
        EntrepotResponse response = entrepotService.updateEntrepot(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactiver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactiverEntrepot(@PathVariable Long id) {
        entrepotService.desactiverEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activerEntrepot(@PathVariable Long id) {
        entrepotService.activerEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ville/{ville}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsByVille(@PathVariable String ville) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsByVille(ville);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nom/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrepotResponse> getEntrepotByNom(@PathVariable String nom) {
        EntrepotResponse response = entrepotService.getEntrepotByNom(nom);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacite/disponible")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntrepotResponse>> getEntrepotsWithAvailableCapacity(
            @RequestParam Double volume) {
        List<EntrepotResponse> response = entrepotService.getEntrepotsWithAvailableCapacity(volume);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/taux-remplissage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> calculerTauxRemplissage(@PathVariable Long id) {
        Double taux = entrepotService.calculerTauxRemplissage(id);
        return ResponseEntity.ok(taux);
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countEntrepotsActifs() {
        long count = entrepotService.countEntrepotsActifs();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/taux-moyen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getAverageTauxRemplissage() {
        Double tauxMoyen = entrepotService.getAverageTauxRemplissage();
        return ResponseEntity.ok(tauxMoyen);
    }
}