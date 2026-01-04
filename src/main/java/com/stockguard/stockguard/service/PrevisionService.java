package com.stockguard.stockguard.service;

import com.stockguard.stockguard.dto.response.PrevisionResponse;

import java.util.List;

public interface PrevisionService {
    PrevisionResponse genererPrevision(Long produitId, Long entrepotId);
    List<PrevisionResponse> getPrevisionsByEntrepot(Long entrepotId);
}
