package com.stockguard.stockguard.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionRequest {
    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    private Long entrepotId;
}
