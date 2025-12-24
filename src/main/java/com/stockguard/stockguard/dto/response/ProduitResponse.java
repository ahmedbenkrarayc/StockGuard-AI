package com.stockguard.stockguard.dto.response;

import com.stockguard.stockguard.model.enums.Unite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponse {

    private Long id;
    private String nom;
    private String description;
    private String categorie;
    private BigDecimal prixVente;
    private BigDecimal prixAchat;
    private BigDecimal marge;
    private BigDecimal poids;
    private Unite unite;
    private Boolean actif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer stockTotal;
    private Integer entrepotsAvecStock;
}
