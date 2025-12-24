package com.stockguard.stockguard.model;

import com.stockguard.stockguard.model.enums.Unite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(min = 2, max = 50, message = "La catégorie doit contenir entre 2 et 50 caractères")
    @Column(nullable = false)
    private String categorie;

    @NotNull(message = "Le prix de vente est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix de vente doit être supérieur à 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private Double prixVente;

    @NotNull(message = "Le prix d'achat est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix d'achat doit être supérieur à 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private Double prixAchat;

    @Column(precision = 5, scale = 2)
    private Double marge;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    @Column(nullable = false, precision = 10, scale = 3)
    private Double poids;

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
    //private List<Stock> stocks;

    //@OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<HistoriqueVente> ventes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (prixAchat != null && prixVente != null && prixAchat > 0) {
            marge = ((prixVente - prixAchat) / prixAchat) * 100;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (prixAchat != null && prixVente != null && prixAchat > 0) {
            marge = ((prixVente - prixAchat) / prixAchat) * 100;
        }
    }
}