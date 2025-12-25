package com.stockguard.stockguard.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRequest {

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    private Long entrepotId;

    @NotNull(message = "La quantité vendue est obligatoire")
    @Min(value = 1, message = "La quantité vendue doit être au moins 1")
    private Integer quantiteVendue;

    @NotNull(message = "Le prix de vente est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix de vente doit être supérieur à 0")
    private BigDecimal prixVente;

    private LocalDate dateVente;
}