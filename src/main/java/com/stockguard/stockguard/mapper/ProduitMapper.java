package com.stockguard.stockguard.mapper;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.model.Produit;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProduitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prixAchatChiffre", ignore = true)
    @Mapping(target = "margeChiffree", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Produit toEntity(ProduitRequest request);

    @Mapping(target = "prixAchat", ignore = true) // Géré manuellement
    @Mapping(target = "marge", ignore = true)    // Géré manuellement
    @Mapping(target = "stockTotal", expression = "java(produit.getStocks() != null ? produit.getStocks().size() : 0)")
    @Mapping(target = "entrepotsAvecStock", expression = "java(produit.getStocks() != null ? " +
            "produit.getStocks().stream().filter(s -> s.getQuantiteDisponible() > 0).count() : 0)")
    ProduitResponse toResponse(Produit produit);
}
