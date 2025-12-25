package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.ApiResponse;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Historique des Ventes", description = "API de gestion de l'historique des ventes")
@RestController
@RequestMapping("/api/historique")
@RequiredArgsConstructor
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    @Operation(summary = "Créer un nouvel historique de vente")
    @PostMapping
    public ResponseEntity<ApiResponse<HistoriqueResponse>> createHistorique(
            @Valid @RequestBody HistoriqueRequest request) {
        try {
            HistoriqueResponse response = historiqueService.createHistorique(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Historique de vente créé avec succès", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer un historique par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriqueResponse>> getHistoriqueById(
            @Parameter(description = "ID de l'historique") @PathVariable Long id) {
        try {
            HistoriqueResponse response = historiqueService.getHistoriqueById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Historique non trouvé: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer tous les historiques de tous les entrepôts")
    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getAllHistoriques() {
        try {
            List<HistoriqueResponse> historiques = historiqueService.getAllHistoriques();
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par entrepôt")
    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByEntrepot(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId) {
        try {
            List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByEntrepot(entrepotId);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par produit")
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByProduit(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        try {
            List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByProduit(produitId);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par période")
    @GetMapping("/periode")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByDateRange(
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByDateRange(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par entrepôt et période")
    @GetMapping("/entrepot/{entrepotId}/periode")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByEntrepotAndDateRange(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<HistoriqueResponse> historiques = historiqueService
                    .getHistoriquesByEntrepotAndDateRange(entrepotId, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par jour de semaine")
    @GetMapping("/jour-semaine/{jourSemaine}")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByJourSemaine(
            @Parameter(description = "Jour de semaine (ex: LUNDI, MARDI, etc.)")
            @PathVariable JourSemaine jourSemaine) {
        try {
            List<HistoriqueResponse> historiques = historiqueService.getHistoriquesByJourSemaine(jourSemaine);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les historiques par entrepôt et jour de semaine")
    @GetMapping("/entrepot/{entrepotId}/jour-semaine/{jourSemaine}")
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> getHistoriquesByEntrepotAndJourSemaine(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId,
            @Parameter(description = "Jour de semaine") @PathVariable JourSemaine jourSemaine) {
        try {
            List<HistoriqueResponse> historiques = historiqueService
                    .getHistoriquesByEntrepotAndJourSemaine(entrepotId, jourSemaine);
            return ResponseEntity.ok(ApiResponse.success(historiques));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir le total des ventes pour un produit et un entrepôt")
    @GetMapping("/stats/total-ventes")
    public ResponseEntity<ApiResponse<Long>> getTotalVentesByProduitAndEntrepot(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        try {
            Long total = historiqueService.getTotalVentesByProduitAndEntrepot(produitId, entrepotId);
            return ResponseEntity.ok(ApiResponse.success("Total des ventes récupéré", total));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors du calcul: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir les ventes par jour de semaine pour un produit et entrepôt")
    @GetMapping("/stats/ventes-par-jour")
    public ResponseEntity<ApiResponse<Map<JourSemaine, Long>>> getVentesParJourSemaine(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        try {
            Map<JourSemaine, Long> ventes = historiqueService.getVentesTotalParJourSemaine(produitId, entrepotId);
            return ResponseEntity.ok(ApiResponse.success(ventes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir le chiffre d'affaires par jour de semaine")
    @GetMapping("/stats/ca-par-jour")
    public ResponseEntity<ApiResponse<Map<JourSemaine, BigDecimal>>> getChiffreAffairesParJourSemaine(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        try {
            Map<JourSemaine, BigDecimal> ca = historiqueService.getChiffreAffairesParJourSemaine(produitId, entrepotId);
            return ResponseEntity.ok(ApiResponse.success(ca));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir le meilleur jour de vente pour un produit")
    @GetMapping("/stats/meilleur-jour")
    public ResponseEntity<ApiResponse<JourSemaine>> getMeilleurJourSemainePourProduit(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId) {
        try {
            JourSemaine meilleurJour = historiqueService.getMeilleurJourSemainePourProduit(produitId, entrepotId);
            return ResponseEntity.ok(ApiResponse.success("Meilleur jour récupéré", meilleurJour));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir les statistiques globales par jour de semaine pour un entrepôt")
    @GetMapping("/stats/globales/{entrepotId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistiquesGlobalesParJourSemaine(
            @Parameter(description = "ID de l'entrepôt") @PathVariable Long entrepotId) {
        try {
            Map<String, Object> stats = historiqueService.getStatistiquesGlobalesParJourSemaine(entrepotId);
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir les ventes par mois pour un produit et entrepôt")
    @GetMapping("/stats/ventes-par-mois")
    public ResponseEntity<ApiResponse<List<Object[]>>> getVentesParMois(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "ID de l'entrepôt") @RequestParam Long entrepotId,
            @Parameter(description = "Année") @RequestParam Integer annee) {
        try {
            List<Object[]> ventes = historiqueService.getVentesParMois(produitId, entrepotId, annee);
            return ResponseEntity.ok(ApiResponse.success(ventes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la récupération: " + e.getMessage()));
        }
    }
}