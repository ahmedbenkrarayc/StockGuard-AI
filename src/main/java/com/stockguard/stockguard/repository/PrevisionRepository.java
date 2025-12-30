package com.stockguard.stockguard.repository;

import com.stockguard.stockguard.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrevisionRepository extends JpaRepository<Prevision, Long> {

    List<Prevision> findByEntrepotIdOrderByDatePrevisionDesc(Long entrepotId);

    Prevision findTopByProduitIdAndEntrepotIdOrderByDatePrevisionDesc(Long produitId, Long entrepotId);
}
