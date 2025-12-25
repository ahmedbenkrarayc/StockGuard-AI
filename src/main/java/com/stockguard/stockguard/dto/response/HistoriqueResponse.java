package com.stockguard.stockguard.dto.response;

import com.stockguard.stockguard.model.enums.JourSemaine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueResponse {

    private Long id;
    private Long produitId;
    private String produitNom;
    private Long entrepotId;
    private String entrepotNom;
    private LocalDate dateVente;
    private Integer quantiteVendue;
    private BigDecimal prixVente;
    private BigDecimal chiffreAffaires;
    private JourSemaine jourSemaine;  // Changé de String à JourSemaine
    private String jourSemaineLibelle; // Pour avoir aussi le libellé en français
    private Integer mois;
    private Integer annee;
    private LocalDateTime createdAt;

    // Ajoute cette méthode pour peupler jourSemaineLibelle automatiquement
    public String getJourSemaineLibelle() {
        return jourSemaine != null ? jourSemaine.getLibelle() : null;
    }
}