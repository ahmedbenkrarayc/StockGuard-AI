package com.stockguard.stockguard.model;

import com.stockguard.stockguard.model.enums.Unite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String categorie;

    @Column(name = "prix_vente", nullable = false, precision = 10, scale = 2)
    private Double prixVente;

    @Column(name = "prix_achat_chiffre", nullable = false, columnDefinition = "TEXT")
    private String prixAchatChiffre;

    @Column(name = "marge_chiffree", nullable = false, columnDefinition = "TEXT")
    private String margeChiffree;

    @Column(nullable = false, precision = 10, scale = 3)
    private Double poids;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Unite unite;

    @Column(name = "code_barre", unique = true, length = 50)
    private String codeBarre;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //@OneToMany(mappedBy = "produit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //private List<Stock> stocks = new ArrayList<>();

    //@OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
    //private List<HistoriqueVente> historiqueVentes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getUniteLibelle() {
        return unite != null ? unite.getLibelle() : "";
    }

    // Méthode pour calculer la marge
    public Double calculerMarge(Double prixAchat) {
        return prixVente - prixAchat;
    }
}