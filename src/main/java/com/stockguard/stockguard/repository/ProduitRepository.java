package com.stockguard.stockguard.repository;

import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.enums.Unite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByActifTrue();

    List<Produit> findByCategorie(String categorie);

    List<Produit> findByNomContainingIgnoreCase(String nom);

    Optional<Produit> findByNom(String nom);

    List<Produit> findByUnite(Unite unite);

    @Query("SELECT p FROM Produit p WHERE p.prixVente BETWEEN :min AND :max")
    List<Produit> findByPrixVenteBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("SELECT DISTINCT p.categorie FROM Produit p WHERE p.categorie IS NOT NULL")
    List<String> findAllCategories();

    long countByActifTrue();
}