package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.ProduitMapper;
import com.stockguard.stockguard.model.Produit;
import com.stockguard.stockguard.model.enums.Unite;
import com.stockguard.stockguard.repository.ProduitRepository;
import com.stockguard.stockguard.service.ProduitService;
import com.stockguard.stockguard.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final EncryptionUtil encryptionUtil;

    @Override
    @Transactional
    public ProduitResponse createProduit(ProduitRequest request) {
        log.info("Création d'un nouveau produit: {}", request.getNom());

        // Vérifier si le produit existe déjà
        produitRepository.findByNom(request.getNom())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Un produit avec le nom '" + request.getNom() + "' existe déjà");
                });

        // Vérifier que le prix de vente > prix d'achat
        if (request.getPrixAchat().compareTo(request.getPrixVente()) >= 0) {
            throw new IllegalArgumentException("Le prix de vente doit être supérieur au prix d'achat");
        }

        Produit produit = produitMapper.toEntity(request);

        // Chiffrer le prix d'achat
        produit.setPrixAchatChiffre(encryptionUtil.encryptBigDecimal(request.getPrixAchat()));

        // Calculer et chiffrer la marge
        BigDecimal marge = calculerMarge(request.getPrixVente(), request.getPrixAchat());
        produit.setMargeChiffree(encryptionUtil.encryptBigDecimal(marge));

        Produit savedProduit = produitRepository.save(produit);

        log.info("Produit créé avec succès: ID {}", savedProduit.getId());
        return createProduitResponse(savedProduit);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitResponse getProduitById(Long id) {
        log.debug("Recherche du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        return createProduitResponse(produit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getAllProduits() {
        log.debug("Récupération de tous les produits");

        return produitRepository.findAll().stream()
                .map(this::createProduitResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getProduitsActifs() {
        log.debug("Récupération des produits actifs");

        return produitRepository.findByActifTrue().stream()
                .map(this::createProduitResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProduitResponse updateProduit(Long id, ProduitRequest request) {
        log.info("Mise à jour du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        // Vérifier si le nouveau nom n'est pas déjà utilisé par un autre produit
        if (!produit.getNom().equals(request.getNom())) {
            produitRepository.findByNom(request.getNom())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalArgumentException("Un autre produit avec le nom '" + request.getNom() + "' existe déjà");
                        }
                    });
        }

        // Vérifier que le prix de vente > prix d'achat
        if (request.getPrixAchat().compareTo(request.getPrixVente()) >= 0) {
            throw new IllegalArgumentException("Le prix de vente doit être supérieur au prix d'achat");
        }

        // Mettre à jour les champs de base
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setCategorie(request.getCategorie());
        produit.setPrixVente(request.getPrixVente());
        produit.setPoids(request.getPoids());
        produit.setUnite(request.getUnite());
        produit.setActif(request.getActif());

        // Chiffrer le nouveau prix d'achat
        produit.setPrixAchatChiffre(encryptionUtil.encryptBigDecimal(request.getPrixAchat()));

        // Recalculer et chiffrer la marge
        BigDecimal marge = calculerMarge(request.getPrixVente(), request.getPrixAchat());
        produit.setMargeChiffree(encryptionUtil.encryptBigDecimal(marge));

        Produit updatedProduit = produitRepository.save(produit);

        log.info("Produit mis à jour avec succès: ID {}", updatedProduit.getId());
        return createProduitResponse(updatedProduit);
    }

    @Override
    @Transactional
    public void deleteProduit(Long id) {
        log.info("Suppression du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        // Vérifier si le produit a des stocks
        if (!produit.getStocks().isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer le produit car il a des stocks associés");
        }

        produitRepository.delete(produit);
        log.info("Produit supprimé avec succès: ID {}", id);
    }

    @Override
    @Transactional
    public void desactiverProduit(Long id) {
        log.info("Désactivation du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        produit.setActif(false);
        produitRepository.save(produit);

        log.info("Produit désactivé avec succès: ID {}", id);
    }

    @Override
    @Transactional
    public void activerProduit(Long id) {
        log.info("Activation du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        produit.setActif(true);
        produitRepository.save(produit);

        log.info("Produit activé avec succès: ID {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getProduitsByCategorie(String categorie) {
        log.debug("Récupération des produits par catégorie: {}", categorie);

        return produitRepository.findByCategorie(categorie).stream()
                .map(this::createProduitResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> searchProduits(String keyword) {
        log.debug("Recherche de produits avec le mot-clé: {}", keyword);

        return produitRepository.findByNomContainingIgnoreCase(keyword).stream()
                .map(this::createProduitResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getProduitsByUnite(Unite unite) {
        log.debug("Récupération des produits par unité: {}", unite);

        return produitRepository.findByUnite(unite).stream()
                .map(this::createProduitResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        log.debug("Récupération de toutes les catégories");

        return produitRepository.findAllCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public long countProduitsActifs() {
        log.debug("Comptage des produits actifs");

        return produitRepository.countByActifTrue();
    }

    private BigDecimal calculerMarge(BigDecimal prixVente, BigDecimal prixAchat) {
        if (prixAchat.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Calcul: ((prixVente - prixAchat) / prixAchat) * 100
        BigDecimal difference = prixVente.subtract(prixAchat);
        BigDecimal marge = difference.divide(prixAchat, 4, RoundingMode.HALF_UP);
        return marge.multiply(new BigDecimal("100"));
    }

    private ProduitResponse createProduitResponse(Produit produit) {
        ProduitResponse response = produitMapper.toResponse(produit);

        // Déchiffrer le prix d'achat et la marge
        try {
            BigDecimal prixAchatDecrypte = encryptionUtil.decryptBigDecimal(produit.getPrixAchatChiffre());
            BigDecimal margeDecryptee = encryptionUtil.decryptBigDecimal(produit.getMargeChiffree());

            response.setPrixAchat(prixAchatDecrypte);
            response.setMarge(margeDecryptee);
        } catch (Exception e) {
            log.error("Erreur lors du déchiffrement pour le produit ID {}: {}", produit.getId(), e.getMessage());
            // En cas d'erreur, on met des valeurs par défaut
            response.setPrixAchat(BigDecimal.ZERO);
            response.setMarge(BigDecimal.ZERO);
        }

        return response;
    }
}