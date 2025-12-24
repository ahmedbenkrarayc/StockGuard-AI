package com.stockguard.stockguard.model;

import com.stockguard.stockguard.model.enums.Unite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nom;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String categorie;

    @NotNull(message = "Le prix de vente est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix de vente doit être supérieur à 0")
    @Column(name = "prix_vente", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixVente;

    @NotNull(message = "Le prix d'achat est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix d'achat doit être supérieur à 0")
    @Column(name = "prix_achat_chiffre", nullable = false, columnDefinition = "TEXT")
    private String prixAchatChiffre;

    @Column(name = "marge_chiffree", columnDefinition = "TEXT")
    private String margeChiffree;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal poids;

    @NotNull(message = "L'unité est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unite unite;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //@OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<Stock> stocks = new ArrayList<>();

    //@OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<HistoriqueVente> ventes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}