package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.StockRequest;
import com.stockguard.stockguard.dto.request.UpdateStockRequest;
import com.stockguard.stockguard.dto.response.StockResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.StockMapper;
import com.stockguard.stockguard.model.Entrepot;
import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.Stock;
import com.stockguard.stockguard.repository.EntrepotRepository;
import com.stockguard.stockguard.repository.ProduitRepository;
import com.stockguard.stockguard.repository.StockRepository;
import com.stockguard.stockguard.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;
    private final StockMapper stockMapper;

    @Override
    @Transactional
    public StockResponse createStock(StockRequest request) {
        log.info("Création d'un nouveau stock pour produit ID {} dans entrepôt ID {}",
                request.getProduitId(), request.getEntrepotId());

        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + request.getProduitId()));

        Entrepot entrepot = entrepotRepository.findById(request.getEntrepotId())
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + request.getEntrepotId()));

        // Vérifier si le stock existe déjà pour ce produit et cet entrepôt
        stockRepository.findByProduitAndEntrepot(produit, entrepot)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Un stock existe déjà pour ce produit dans cet entrepôt");
                });

        // Vérifier les seuils
        if (request.getSeuilAlerte() >= request.getStockMaximum()) {
            throw new IllegalArgumentException("Le seuil d'alerte doit être inférieur au stock maximum");
        }

        // Vérifier la capacité de l'entrepôt
        double volumeRequise = produit.getPoids().doubleValue() * request.getQuantiteDisponible() * 1.5;
        if (!entrepot.aDeLaCapacite(volumeRequise)) {
            throw new IllegalStateException("L'entrepôt n'a pas assez de capacité disponible");
        }

        Stock stock = stockMapper.toEntity(request);
        stock.setProduit(produit);
        stock.setEntrepot(entrepot);
        stock.calculerVolumeOccupe();

        // Mettre à jour la capacité utilisée de l'entrepôt
        entrepot.setCapaciteUtilisee(entrepot.getCapaciteUtilisee() + stock.getVolumeOccupe());
        entrepot.calculerTauxRemplissage();
        entrepotRepository.save(entrepot);

        Stock savedStock = stockRepository.save(stock);

        log.info("Stock créé avec succès: ID {}", savedStock.getId());
        return stockMapper.toResponse(savedStock);
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockById(Long id) {
        log.debug("Recherche du stock avec ID: {}", id);

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec ID: " + id));

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getAllStocks() {
        log.debug("Récupération de tous les stocks");

        return stockRepository.findAll().stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getStocksByEntrepot(Long entrepotId) {
        log.debug("Récupération des stocks pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return stockRepository.findByEntrepotId(entrepotId).stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getStocksByProduit(Long produitId) {
        log.debug("Récupération des stocks pour le produit ID: {}", produitId);

        // Vérifier que le produit existe
        produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + produitId));

        return stockRepository.findByProduitId(produitId).stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockResponse updateStock(Long id, StockRequest request) {
        log.info("Mise à jour du stock avec ID: {}", id);

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec ID: " + id));

        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + request.getProduitId()));

        Entrepot entrepot = entrepotRepository.findById(request.getEntrepotId())
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + request.getEntrepotId()));

        // Vérifier les seuils
        if (request.getSeuilAlerte() >= request.getStockMaximum()) {
            throw new IllegalArgumentException("Le seuil d'alerte doit être inférieur au stock maximum");
        }

        // Si l'entrepôt change, vérifier la capacité
        if (!stock.getEntrepot().getId().equals(entrepot.getId())) {
            double volumeRequise = produit.getPoids().doubleValue() * request.getQuantiteDisponible() * 1.5;
            if (!entrepot.aDeLaCapacite(volumeRequise)) {
                throw new IllegalStateException("Le nouvel entrepôt n'a pas assez de capacité disponible");
            }

            // Libérer la capacité de l'ancien entrepôt
            Entrepot ancienEntrepot = stock.getEntrepot();
            ancienEntrepot.setCapaciteUtilisee(ancienEntrepot.getCapaciteUtilisee() - stock.getVolumeOccupe());
            ancienEntrepot.calculerTauxRemplissage();
            entrepotRepository.save(ancienEntrepot);

            // Réserver la capacité du nouvel entrepôt
            entrepot.setCapaciteUtilisee(entrepot.getCapaciteUtilisee() + volumeRequise);
            entrepot.calculerTauxRemplissage();
            entrepotRepository.save(entrepot);
        }

        stock.setProduit(produit);
        stock.setEntrepot(entrepot);
        stock.setQuantiteDisponible(request.getQuantiteDisponible());
        stock.setSeuilAlerte(request.getSeuilAlerte());
        stock.setQuantiteSecurite(request.getQuantiteSecurite());
        stock.setStockMaximum(request.getStockMaximum());
        stock.calculerVolumeOccupe();

        Stock updatedStock = stockRepository.save(stock);

        log.info("Stock mis à jour avec succès: ID {}", updatedStock.getId());
        return stockMapper.toResponse(updatedStock);
    }

    @Override
    @Transactional
    public void deleteStock(Long id) {
        log.info("Suppression du stock avec ID: {}", id);

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec ID: " + id));

        // Libérer la capacité de l'entrepôt
        Entrepot entrepot = stock.getEntrepot();
        entrepot.setCapaciteUtilisee(entrepot.getCapaciteUtilisee() - stock.getVolumeOccupe());
        entrepot.calculerTauxRemplissage();
        entrepotRepository.save(entrepot);

        stockRepository.delete(stock);
        log.info("Stock supprimé avec succès: ID {}", id);
    }

    @Override
    @Transactional
    public StockResponse ajouterQuantite(UpdateStockRequest request) {
        log.info("Ajout de quantité au stock ID {}: {} unités",
                request.getStockId(), request.getQuantite());

        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec ID: " + request.getStockId()));

        // Vérifier si on peut ajouter la quantité
        if (!stock.peutAjouterQuantite(request.getQuantite())) {
            throw new IllegalStateException("Impossible d'ajouter cette quantité, dépasse le stock maximum");
        }

        // Vérifier la capacité de l'entrepôt
        double volumeAdditionnel = stock.getProduit().getPoids().doubleValue() * request.getQuantite() * 1.5;
        if (!stock.getEntrepot().aDeLaCapacite(volumeAdditionnel)) {
            throw new IllegalStateException("L'entrepôt n'a pas assez de capacité disponible");
        }

        // Mettre à jour le stock
        stock.setQuantiteDisponible(stock.getQuantiteDisponible() + request.getQuantite());
        stock.calculerVolumeOccupe();
        stock.setUpdatedAt(LocalDateTime.now());

        // Mettre à jour la capacité de l'entrepôt
        Entrepot entrepot = stock.getEntrepot();
        entrepot.setCapaciteUtilisee(entrepot.getCapaciteUtilisee() + volumeAdditionnel);
        entrepot.calculerTauxRemplissage();
        entrepotRepository.save(entrepot);

        Stock updatedStock = stockRepository.save(stock);

        log.info("Quantité ajoutée avec succès au stock ID {}", updatedStock.getId());
        return stockMapper.toResponse(updatedStock);
    }

    @Override
    @Transactional
    public StockResponse retirerQuantite(UpdateStockRequest request) {
        log.info("Retrait de quantité du stock ID {}: {} unités",
                request.getStockId(), request.getQuantite());

        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé avec ID: " + request.getStockId()));

        // Vérifier si on peut retirer la quantité
        if (!stock.peutRetirerQuantite(request.getQuantite())) {
            throw new IllegalStateException("Stock insuffisant pour effectuer le retrait");
        }

        // Mettre à jour le stock
        stock.setQuantiteDisponible(stock.getQuantiteDisponible() - request.getQuantite());
        stock.calculerVolumeOccupe();
        stock.setUpdatedAt(LocalDateTime.now());

        // Libérer la capacité de l'entrepôt
        double volumeLibere = stock.getProduit().getPoids().doubleValue() * request.getQuantite() * 1.5;
        Entrepot entrepot = stock.getEntrepot();
        entrepot.setCapaciteUtilisee(Math.max(0, entrepot.getCapaciteUtilisee() - volumeLibere));
        entrepot.calculerTauxRemplissage();
        entrepotRepository.save(entrepot);

        Stock updatedStock = stockRepository.save(stock);

        log.info("Quantité retirée avec succès du stock ID {}", updatedStock.getId());
        return stockMapper.toResponse(updatedStock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getStocksFaiblesByEntrepot(Long entrepotId) {
        log.debug("Récupération des stocks faibles pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return stockRepository.findStocksFaiblesByEntrepot(entrepotId).stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getAllStocksFaibles() {
        log.debug("Récupération de tous les stocks faibles");

        return stockRepository.findAllStocksFaibles().stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getStocksEnRuptureByEntrepot(Long entrepotId) {
        log.debug("Récupération des stocks en rupture pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return stockRepository.findStocksEnRuptureByEntrepot(entrepotId).stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getSurStocks() {
        log.debug("Récupération des sur-stocks");

        return stockRepository.findSurStocks().stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockByProduitAndEntrepot(Long produitId, Long entrepotId) {
        log.debug("Recherche du stock pour produit ID {} dans entrepôt ID {}", produitId, entrepotId);

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + produitId));

        Entrepot entrepot = entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        Stock stock = stockRepository.findByProduitAndEntrepot(produit, entrepot)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trouvé pour ce produit et cet entrepôt"));

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculerValeurStockByEntrepot(Long entrepotId) {
        log.debug("Calcul de la valeur du stock pour l'entrepôt ID: {}", entrepotId);

        // Vérifier que l'entrepôt existe
        entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        BigDecimal valeur = stockRepository.calculateValeurStockByEntrepot(entrepotId);
        return valeur != null ? valeur : BigDecimal.ZERO;
    }
}