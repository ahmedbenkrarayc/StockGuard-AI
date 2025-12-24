package com.stockguard.stockguard.model;

import com.stockguard.stockguard.model.enums.JourSemaine;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_ventes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le produit est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @NotNull(message = "L'entrepôt est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    @NotNull(message = "La date de vente est obligatoire")
    @Column(name = "date_vente", nullable = false)
    private LocalDate dateVente;

    @NotNull(message = "La quantité vendue est obligatoire")
    @Min(value = 1)
    @Column(name = "quantite_vendue", nullable = false)
    private Integer quantiteVendue;

    @NotNull(message = "Le prix de vente est obligatoire")
    @DecimalMin(value = "0.01")
    @Column(name = "prix_vente", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixVente;

    @Column(name = "chiffre_affaires", nullable = false, precision = 10, scale = 2)
    private BigDecimal chiffreAffaires;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jour_semaine", nullable = false)
    private JourSemaine jourSemaine;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(nullable = false)
    private Integer mois;

    @NotNull
    @Min(value = 2000)
    @Column(nullable = false)
    private Integer annee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dateVente == null) {
            dateVente = LocalDate.now();
        }

        // Utiliser l'enum JourSemaine
        jourSemaine = JourSemaine.fromLocalDate(dateVente);
        mois = dateVente.getMonthValue();
        annee = dateVente.getYear();

        if (quantiteVendue != null && prixVente != null) {
            chiffreAffaires = prixVente.multiply(BigDecimal.valueOf(quantiteVendue));
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (dateVente != null) {
            jourSemaine = JourSemaine.fromLocalDate(dateVente);
            mois = dateVente.getMonthValue();
            annee = dateVente.getYear();
        }
    }
}