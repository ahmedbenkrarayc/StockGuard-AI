package com.stockguard.stockguard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockRequest {

    @NotNull(message = "L'ID du stock est obligatoire")
    private Long stockId;

    @NotNull(message = "La quantité est obligatoire")
    private Integer quantite;

    private String typeOperation;
    private String raison;
}