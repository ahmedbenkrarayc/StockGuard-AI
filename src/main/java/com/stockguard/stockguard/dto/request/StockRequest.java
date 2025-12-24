package com.stockguard.stockguard.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    private Long entrepotId;

    @NotNull(message = "La quantité disponible est obligatoire")
    @Min(value = 0, message = "La quantité disponible ne peut pas être négative")
    private Integer quantiteDisponible;

    @NotNull(message = "Le seuil d'alerte est obligatoire")
    @Min(value = 0, message = "Le seuil d'alerte ne peut pas être négatif")
    private Integer seuilAlerte = 10;

    @NotNull(message = "La quantité de sécurité est obligatoire")
    @Min(value = 0, message = "La quantité de sécurité ne peut pas être négative")
    private Integer quantiteSecurite = 5;

    @NotNull(message = "Le stock maximum est obligatoire")
    @Min(value = 1, message = "Le stock maximum doit être au moins 1")
    private Integer stockMaximum = 1000;
}