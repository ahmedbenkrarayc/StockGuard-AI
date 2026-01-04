package com.stockguard.stockguard.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionResponse {
    private Long id;
    private String produitNom;
    private String entrepotNom;
    private LocalDate datePrevision;
    private Integer quantitePrevue30Jours;
    private Integer niveauConfiance;
    private String recommandation;
}
