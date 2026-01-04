package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.HistoriqueResponse;
import com.stockguard.stockguard.model.enums.JourSemaine;
import com.stockguard.stockguard.service.HistoriqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Historique des Ventes", description = "API de gestion de l'historique des ventes")
@RestController
@RequestMapping("/historique")
@RequiredArgsConstructor
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    @Operation(summary = "Créer un nouvel historique de vente")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HistoriqueResponse> createHistorique(@Valid @RequestBody HistoriqueRequest request) {
        HistoriqueResponse response = historiqueService.createHistorique(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Récupérer un historique par son ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HistoriqueResponse> getHistoriqueById(
            @Parameter(description = "ID de l'historique") @PathVariable Long id) {
        HistoriqueResponse response = historiqueService.getHistoriqueById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer tous les historiques de tous les entrepôts")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getAllHistoriques() {
        List<HistoriqueResponse> historiques = historiqueService.getAllHistoriques();
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par entrepôt")
    @GetMapping("/entrepot/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByEntrepot(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId) {
        List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByEntrepot(entrepotId);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par produit")
    @GetMapping("/produit/{produitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByProduit(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByProduit(produitId);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par période")
    @GetMapping("/periode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByDateRange(startDate, endDate);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par entrepôt et période")
    @GetMapping("/entrepot/{entrepotId}/periode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByEntrepotAndDateRange(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HistoriqueResponse> historiques = historiqueService
                .getHistoriquesByEntrepotAndDateRange(entrepotId, startDate, endDate);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par jour de semaine")
    @GetMapping("/jour-semaine/{jourSemaine}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByJourSemaine(
            @Parameter(description = "Jour de semaine (ex: LUNDI, MARDI, etc.)")
            @PathVariable JourSemaine jourSemaine) {
        List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByJourSemaine(jourSemaine);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Récupérer les historiques par entrepôt et jour de semaine")
    @GetMapping("/entrepot/{entrepotId}/jour-semaine/{jourSemaine}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistoriqueResponse>> getHistoriquesByEntrepotAndJourSemaine(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId,
            @Parameter(description = "Jour de semaine") @PathVariable JourSemaine jourSemaine) {
        List<HistoriqueResponse> historiques = historiqueService
                .getHistoriquesByEntrepotAndJourSemaine(entrepotId, jourSemaine);
        return ResponseEntity.ok(historiques);
    }

    @Operation(summary = "Obtenir le total des ventes pour un produit et un entrepôt")
    @GetMapping("/stats/total-ventes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalVentesByProduitAndEntrepot(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        Long total = historiqueService.getTotalVentesByProduitAndEntrepot(produitId, entrepotId);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Obtenir les ventes par jour de semaine pour un produit et entrepôt")
    @GetMapping("/stats/ventes-par-jour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<JourSemaine, Long>> getVentesParJourSemaine(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        Map<JourSemaine, Long> ventes = historiqueService.getVentesTotalParJourSemaine(produitId, entrepotId);
        return ResponseEntity.ok(ventes);
    }

    @Operation(summary = "Obtenir le chiffre d'affaires par jour de semaine")
    @GetMapping("/stats/ca-par-jour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<JourSemaine, BigDecimal>> getChiffreAffairesParJourSemaine(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        Map<JourSemaine, BigDecimal> ca = historiqueService.getChiffreAffairesParJourSemaine(produitId, entrepotId);
        return ResponseEntity.ok(ca);
    }

    @Operation(summary = "Obtenir le meilleur jour de vente pour un produit")
    @GetMapping("/stats/meilleur-jour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JourSemaine> getMeilleurJourSemainePourProduit(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        JourSemaine meilleurJour = historiqueService.getMeilleurJourSemainePourProduit(produitId, entrepotId);
        return ResponseEntity.ok(meilleurJour);
    }

    @Operation(summary = "Obtenir les statistiques globales par jour de semaine pour un entrepôt")
    @GetMapping("/stats/globales/{entrepotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistiquesGlobalesParJourSemaine(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId) {
        Map<String, Object> stats = historiqueService.getStatistiquesGlobalesParJourSemaine(entrepotId);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Obtenir les ventes par mois pour un produit et entrepôt")
    @GetMapping("/stats/ventes-par-mois")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getVentesParMois(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId,
            @Parameter(description = "Année") @RequestParam Integer annee) {
        List<Object[]> ventes = historiqueService.getVentesParMois(produitId, entrepotId, annee);
        return ResponseEntity.ok(ventes);
    }
}