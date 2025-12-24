package com.stockguard.stockguard.dto.request;

import com.stockguard.stockguard.model.enums.Unite;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100)
    private String nom;

    @Size(max = 500)
    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(min = 2, max = 50)
    private String categorie;

    @NotNull(message = "Le prix de vente est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix de vente doit être supérieur à 0")
    private BigDecimal prixVente;

    @NotNull(message = "Le prix d'achat est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix d'achat doit être supérieur à 0")
    private BigDecimal prixAchat;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    @NotNull(message = "L'unité est obligatoire")
    private Unite unite;

    private Boolean actif = true;
}