package com.stockguard.stockguard.mapper;

import com.stockguard.stockguard.dto.request.StockRequest;
import com.stockguard.stockguard.dto.response.StockResponse;
import com.stockguard.stockguard.model.Stock;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produit", ignore = true)
    @Mapping(target = "entrepot", ignore = true)
    @Mapping(target = "volumeOccupe", constant = "0.0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Stock toEntity(StockRequest request);

    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    @Mapping(target = "produitCategorie", source = "produit.categorie")
    @Mapping(target = "entrepotId", source = "entrepot.id")
    @Mapping(target = "entrepotNom", source = "entrepot.nom")
    @Mapping(target = "stockFaible", expression = "java(stock.isStockFaible())")
    @Mapping(target = "enRupture", expression = "java(stock.isEnRupture())")
    @Mapping(target = "surStock", expression = "java(stock.isSurStock())")
    @Mapping(target = "valeurStock", expression = "java(stock.getProduit().getPrixVente().multiply(java.math.BigDecimal.valueOf(stock.getQuantiteDisponible())))")
    StockResponse toResponse(Stock stock);
}