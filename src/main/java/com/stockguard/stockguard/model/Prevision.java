package com.stockguard.stockguard.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "previsions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    @Column(name = "date_prevision", nullable = false)
    private LocalDate datePrevision;

    @Column(name = "quantite_prevue_30_jours")
    private Integer quantitePrevue30Jours;

    @Column(name = "niveau_confiance")
    private Integer niveauConfiance; // En pourcentage

    @Column(columnDefinition = "TEXT")
    private String recommandation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (datePrevision == null) {
            datePrevision = LocalDate.now();
        }
    }
}
