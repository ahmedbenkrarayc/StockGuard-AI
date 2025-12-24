package com.stockguard.stockguard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produit_seq")
    @SequenceGenerator(name = "produit_seq", sequenceName = "produit_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String categorie;

    @Column(name = "prix_vente", nullable = false)
    private Double prixVente;

    @Column(name = "prix_achat_chiffre", nullable = false, columnDefinition = "TEXT")
    private String prixAchatChiffre;

    @Column(name = "marge_chiffree", nullable = false, columnDefinition = "TEXT")
    private String margeChiffree;

    @Column(nullable = false)
    private Double poids;

    @Column(nullable = false)
    private String unite;

    @Column(name = "code_barre", unique = true)
    private String codeBarre;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}