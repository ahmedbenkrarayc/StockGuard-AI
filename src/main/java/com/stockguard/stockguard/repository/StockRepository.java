package com.stockguard.stockguard.repository;

import com.stockguard.stockguard.model.Entrepot;
import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProduitAndEntrepot(Produit produit, Entrepot entrepot);

    List<Stock> findByEntrepot(Entrepot entrepot);

    List<Stock> findByProduit(Produit produit);

    List<Stock> findByEntrepotId(Long entrepotId);

    List<Stock> findByProduitId(Long produitId);

    @Query("SELECT s FROM Stock s WHERE s.entrepot.id = :entrepotId AND s.quantiteDisponible <= s.seuilAlerte")
    List<Stock> findStocksFaiblesByEntrepot(@Param("entrepotId") Long entrepotId);

    @Query("SELECT s FROM Stock s WHERE s.quantiteDisponible <= s.seuilAlerte")
    List<Stock> findAllStocksFaibles();

    @Query("SELECT s FROM Stock s WHERE s.entrepot.id = :entrepotId AND s.quantiteDisponible = 0")
    List<Stock> findStocksEnRuptureByEntrepot(@Param("entrepotId") Long entrepotId);

    @Query("SELECT s FROM Stock s WHERE s.quantiteDisponible > s.stockMaximum * 0.9")
    List<Stock> findSurStocks();

    @Query("SELECT s FROM Stock s WHERE s.entrepot.id = :entrepotId ORDER BY s.quantiteDisponible ASC")
    List<Stock> findByEntrepotOrderByQuantiteDisponibleAsc(@Param("entrepotId") Long entrepotId);

    @Query("SELECT SUM(s.quantiteDisponible * p.prixVente) FROM Stock s JOIN s.produit p WHERE s.entrepot.id = :entrepotId")
    BigDecimal calculateValeurStockByEntrepot(@Param("entrepotId") Long entrepotId);
}