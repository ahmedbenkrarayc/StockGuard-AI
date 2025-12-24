package com.stockguard.stockguard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrepots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrepot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'entrepôt est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false, unique = true)
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String ville;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(min = 5, max = 200)
    @Column(nullable = false)
    private String adresse;

    @Size(max = 10)
    @Column(name = "code_postal")
    private String codePostal;

    @Size(min = 8, max = 20)
    private String telephone;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull(message = "La capacité totale est obligatoire")
    @DecimalMin(value = "1")
    @Column(name = "capacite_totale", nullable = false)
    private Double capaciteTotale;

    @Column(name = "capacite_utilisee", nullable = false)
    private Double capaciteUtilisee = 0.0;

    @Column(name = "taux_remplissage", nullable = false)
    private Double tauxRemplissage = 0.0;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "entrepot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "entrepot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueVente> ventes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculerTauxRemplissage();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculerTauxRemplissage();
    }

    public void calculerTauxRemplissage() {
        if (capaciteTotale != null && capaciteTotale > 0) {
            tauxRemplissage = (capaciteUtilisee / capaciteTotale) * 100;
        }
    }

    public boolean aDeLaCapacite(Double volumeRequise) {
        return (capaciteTotale - capaciteUtilisee) >= volumeRequise;
    }
}