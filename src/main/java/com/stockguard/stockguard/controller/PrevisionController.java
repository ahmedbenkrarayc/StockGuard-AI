package com.stockguard.stockguard.controller;

import com.stockguard.stockguard.dto.request.PrevisionRequest;
import com.stockguard.stockguard.dto.response.PrevisionResponse;
import com.stockguard.stockguard.service.PrevisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/previsions")
@RequiredArgsConstructor
@Tag(name = "IA & Prévisions", description = "Prévisions de stock basées sur Gemini AI")
public class PrevisionController {

    private final PrevisionService previsionService;

    @Operation(summary = "Générer une prévision (Admin/Gestionnaire)")
    @PostMapping("/generer")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<PrevisionResponse> genererPrevision(@Valid @RequestBody PrevisionRequest request) {
        return ResponseEntity.ok(previsionService.genererPrevision(request.getProduitId(), request.getEntrepotId()));
    }

    @Operation(summary = "Historique des prévisions par entrepôt")
    @GetMapping("/entrepot/{entrepotId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<List<PrevisionResponse>> getPrevisionsByEntrepot(@PathVariable Long entrepotId) {
        return ResponseEntity.ok(previsionService.getPrevisionsByEntrepot(entrepotId));
    }
}