package com.stockguard.stockguard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"produit_id", "entrepot_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

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

    @NotNull(message = "La quantité disponible est obligatoire")
    @Min(value = 0)
    @Column(name = "quantite_disponible", nullable = false)
    private Integer quantiteDisponible = 0;

    @NotNull(message = "Le seuil d'alerte est obligatoire")
    @Min(value = 0)
    @Column(name = "seuil_alerte", nullable = false)
    private Integer seuilAlerte = 10;

    @NotNull(message = "La quantité de sécurité est obligatoire")
    @Min(value = 0)
    @Column(name = "quantite_securite", nullable = false)
    private Integer quantiteSecurite = 5;

    @NotNull(message = "Le stock maximum est obligatoire")
    @Min(value = 1)
    @Column(name = "stock_maximum", nullable = false)
    private Integer stockMaximum = 1000;

    @Column(name = "volume_occupe", nullable = false)
    private Double volumeOccupe = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculerVolumeOccupe();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculerVolumeOccupe();
    }

    public void calculerVolumeOccupe() {
        if (produit != null && quantiteDisponible != null && produit.getPoids() != null) {
            volumeOccupe = produit.getPoids().doubleValue() * quantiteDisponible * 1.5;
        }
    }

    public boolean isStockFaible() {
        return quantiteDisponible <= seuilAlerte;
    }

    public boolean isEnRupture() {
        return quantiteDisponible <= 0;
    }

    public boolean isSurStock() {
        return quantiteDisponible > stockMaximum * 0.9;
    }

    public boolean peutAjouterQuantite(Integer quantite) {
        return (quantiteDisponible + quantite) <= stockMaximum;
    }

    public boolean peutRetirerQuantite(Integer quantite) {
        return (quantiteDisponible - quantite) >= 0;
    }
}