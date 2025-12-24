package com.stockguard.stockguard.mapper;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
import com.stockguard.stockguard.dto.response.EntrepotResponse;
import com.stockguard.stockguard.model.Entrepot;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EntrepotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "ventes", ignore = true)
    @Mapping(target = "capaciteUtilisee", constant = "0.0")
    @Mapping(target = "tauxRemplissage", constant = "0.0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Entrepot toEntity(EntrepotRequest request);

    @Mapping(target = "nombreProduits", expression = "java(entrepot.getStocks() != null ? entrepot.getStocks().size() : 0)")
    @Mapping(target = "nombreStocksFaibles", expression = "java(entrepot.getStocks() != null ? (int) entrepot.getStocks().stream().filter(s -> s.isStockFaible()).count() : 0)")
    @Mapping(target = "nombreGestionnaires", constant = "0")
    EntrepotResponse toResponse(Entrepot entrepot);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "ventes", ignore = true)
    @Mapping(target = "capaciteUtilisee", ignore = true)
    @Mapping(target = "tauxRemplissage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(EntrepotRequest request, @MappingTarget Entrepot entrepot);
}