package com.stockguard.stockguard.mapper;

import com.stockguard.stockguard.dto.request.HistoriqueRequest;
import com.stockguard.stockguard.dto.response.HistoriqueResponse;
import com.stockguard.stockguard.model.HistoriqueVente;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HistoriqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produit", ignore = true)
    @Mapping(target = "entrepot", ignore = true)
    @Mapping(target = "chiffreAffaires", ignore = true)
    @Mapping(target = "jourSemaine", ignore = true)  // Sera calculé dans @PrePersist
    @Mapping(target = "mois", ignore = true)         // Sera calculé dans @PrePersist
    @Mapping(target = "annee", ignore = true)        // Sera calculé dans @PrePersist
    @Mapping(target = "createdAt", ignore = true)
    HistoriqueVente toEntity(HistoriqueRequest request);

    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    @Mapping(target = "entrepotId", source = "entrepot.id")
    @Mapping(target = "entrepotNom", source = "entrepot.nom")
    @Mapping(target = "jourSemaineLibelle", expression = "java(historique.getJourSemaine() != null ? historique.getJourSemaine().getLibelle() : null)")
    HistoriqueResponse toResponse(HistoriqueVente historique);
}