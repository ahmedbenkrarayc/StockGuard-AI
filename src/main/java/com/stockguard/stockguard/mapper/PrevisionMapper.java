package com.stockguard.stockguard.mapper;


import com.stockguard.stockguard.dto.response.PrevisionResponse;
import com.stockguard.stockguard.model.Prevision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrevisionMapper {

    @Mapping(target = "produitNom", source = "produit.nom")
    @Mapping(target = "entrepotNom", source = "entrepot.nom")
    PrevisionResponse toResponse(Prevision prevision);
}
