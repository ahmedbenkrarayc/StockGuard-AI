package com.stockguard.stockguard.repository;

import com.stockguard.stockguard.model.HistoriqueVente;
import com.stockguard.stockguard.model.enums.JourSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<HistoriqueVente, Long> {

    List<HistoriqueVente> findByEntrepotId(Long entrepotId);

    List<HistoriqueVente> findByProduitId(Long produitId);

    List<HistoriqueVente> findByDateVenteBetween(LocalDate startDate, LocalDate endDate);

    List<HistoriqueVente> findByEntrepotIdAndDateVenteBetween(Long entrepotId, LocalDate startDate, LocalDate endDate);

    List<HistoriqueVente> findByProduitIdAndEntrepotIdAndDateVenteBetween(
            Long produitId, Long entrepotId, LocalDate startDate, LocalDate endDate);

    // Nouvelle méthode : Rechercher par jour de semaine
    List<HistoriqueVente> findByJourSemaine(JourSemaine jourSemaine);

    // Rechercher par jour de semaine et entrepôt
    List<HistoriqueVente> findByEntrepotIdAndJourSemaine(Long entrepotId, JourSemaine jourSemaine);

    // Rechercher par jour de semaine, produit et entrepôt
    List<HistoriqueVente> findByProduitIdAndEntrepotIdAndJourSemaine(
            Long produitId, Long entrepotId, JourSemaine jourSemaine);

    @Query("SELECT h FROM HistoriqueVente h WHERE h.entrepot.id = :entrepotId AND h.dateVente >= :date")
    List<HistoriqueVente> findRecentByEntrepot(@Param("entrepotId") Long entrepotId, @Param("date") LocalDate date);

    @Query("SELECT SUM(h.quantiteVendue) FROM HistoriqueVente h WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId")
    Long getTotalVentesByProduitAndEntrepot(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId);

    @Query("SELECT SUM(h.chiffreAffaires) FROM HistoriqueVente h WHERE h.entrepot.id = :entrepotId AND h.dateVente BETWEEN :start AND :end")
    BigDecimal getChiffreAffairesByEntrepotAndPeriod(
            @Param("entrepotId") Long entrepotId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT h.jourSemaine, SUM(h.quantiteVendue) FROM HistoriqueVente h " +
            "WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId " +
            "GROUP BY h.jourSemaine ORDER BY h.jourSemaine")
    List<Object[]> getVentesParJourSemaine(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId);

    @Query("SELECT h.jourSemaine, SUM(h.chiffreAffaires) FROM HistoriqueVente h " +
            "WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId " +
            "GROUP BY h.jourSemaine ORDER BY h.jourSemaine")
    List<Object[]> getChiffreAffairesParJourSemaine(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId);

    @Query("SELECT h.mois, SUM(h.quantiteVendue) FROM HistoriqueVente h " +
            "WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId AND h.annee = :annee " +
            "GROUP BY h.mois ORDER BY h.mois")
    List<Object[]> getVentesParMois(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId, @Param("annee") Integer annee);

    @Query("SELECT h.mois, SUM(h.chiffreAffaires) FROM HistoriqueVente h " +
            "WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId AND h.annee = :annee " +
            "GROUP BY h.mois ORDER BY h.mois")
    List<Object[]> getChiffreAffairesParMois(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId, @Param("annee") Integer annee);

    @Query("SELECT h.jourSemaine, AVG(h.quantiteVendue) FROM HistoriqueVente h " +
            "WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId " +
            "GROUP BY h.jourSemaine ORDER BY h.jourSemaine")
    List<Object[]> getMoyenneVentesParJourSemaine(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId);

    // Statistiques globales par jour de semaine
    @Query("SELECT h.jourSemaine, SUM(h.quantiteVendue), SUM(h.chiffreAffaires) " +
            "FROM HistoriqueVente h WHERE h.entrepot.id = :entrepotId " +
            "GROUP BY h.jourSemaine ORDER BY h.jourSemaine")
    List<Object[]> getStatistiquesGlobalesParJourSemaine(@Param("entrepotId") Long entrepotId);

    // Meilleur jour de vente pour un produit
    @Query("SELECT h.jourSemaine, SUM(h.quantiteVendue) as total " +
            "FROM HistoriqueVente h WHERE h.produit.id = :produitId AND h.entrepot.id = :entrepotId " +
            "GROUP BY h.jourSemaine ORDER BY total DESC LIMIT 1")
    List<Object[]> getMeilleurJourSemainePourProduit(@Param("produitId") Long produitId, @Param("entrepotId") Long entrepotId);
}
