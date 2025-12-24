package com.stockguard.stockguard.repository;

import com.stockguard.stockguard.model.Entrepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrepotRepository extends JpaRepository<Entrepot, Long> {

    List<Entrepot> findByActifTrue();

    Optional<Entrepot> findByNom(String nom);

    List<Entrepot> findByVille(String ville);

    List<Entrepot> findByTauxRemplissageLessThan(Double taux);

    List<Entrepot> findByTauxRemplissageGreaterThan(Double taux);

    @Query("SELECT e FROM Entrepot e WHERE e.capaciteTotale - e.capaciteUtilisee >= :volume")
    List<Entrepot> findWithAvailableCapacity(@Param("volume") Double volume);

    @Query("SELECT COUNT(e) FROM Entrepot e WHERE e.actif = true")
    long countActifs();

    @Query("SELECT AVG(e.tauxRemplissage) FROM Entrepot e WHERE e.actif = true")
    Double findAverageTauxRemplissage();
}