package com.stockguard.stockguard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrepotResponse {

    private Long id;
    private String nom;
    private String ville;
    private String adresse;
    private String codePostal;
    private String telephone;
    private String email;
    private Double capaciteTotale;
    private Double capaciteUtilisee;
    private Double tauxRemplissage;
    private Boolean actif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer nombreProduits;
    private Integer nombreStocksFaibles;
    private Integer nombreGestionnaires;
}