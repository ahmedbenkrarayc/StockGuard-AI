package com.stockguard.stockguard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {

    private Long id;
    private Long produitId;
    private String produitNom;
    private String produitCategorie;
    private Long entrepotId;
    private String entrepotNom;
    private Integer quantiteDisponible;
    private Integer seuilAlerte;
    private Integer quantiteSecurite;
    private Integer stockMaximum;
    private Double volumeOccupe;
    private Boolean stockFaible;
    private Boolean enRupture;
    private Boolean surStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal valeurStock;
}