package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.ProduitRequest;
import com.stockguard.stockguard.dto.response.ProduitResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.ProduitMapper;
import com.stockguard.stockguard.model.Produit;
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
        if (produitRepository.findByNom(request.getNom()).isPresent()) {
            throw new IllegalArgumentException("Un produit avec le nom '" + request.getNom() + "' existe déjà");
        }

        // Vérifier les prix avec BigDecimal
        if (request.getPrixAchat().compareTo(request.getPrixVente()) >= 0) {
            throw new IllegalArgumentException("Le prix de vente doit être supérieur au prix d'achat");
        }

        Produit produit = produitMapper.toEntity(request);

        // Chiffrer le prix d'achat (BigDecimal)
        produit.setPrixAchatChiffre(encryptionUtil.encryptBigDecimal(request.getPrixAchat()));

        // Calculer et chiffrer la marge (BigDecimal)
        BigDecimal marge = calculerMarge(request.getPrixVente(), request.getPrixAchat());
        produit.setMargeChiffree(encryptionUtil.encryptBigDecimal(marge));

        Produit savedProduit = produitRepository.save(produit);

        log.info("Produit créé avec succès: ID {}", savedProduit.getId());

        // Retourner la réponse sans les données sensibles
        return createResponseForRole(savedProduit, "USER");
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitResponse getProduitById(Long id, String role) {
        log.debug("Recherche du produit avec ID: {} pour le rôle: {}", id, role);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        return createResponseForRole(produit, role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getAllProduits(String role) {
        log.debug("Récupération de tous les produits pour le rôle: {}", role);

        return produitRepository.findAll().stream()
                .map(produit -> createResponseForRole(produit, role))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getProduitsActifs(String role) {
        log.debug("Récupération des produits actifs pour le rôle: {}", role);

        return produitRepository.findByActifTrue().stream()
                .map(produit -> createResponseForRole(produit, role))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProduitResponse updateProduit(Long id, ProduitRequest request, String role) {
        log.info("Mise à jour du produit avec ID: {}", id);

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID: " + id));

        // Vérifier si le nouveau nom n'est pas déjà utilisé
        if (!produit.getNom().equals(request.getNom())) {
            produitRepository.findByNom(request.getNom())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalArgumentException("Un autre produit avec le nom '" + request.getNom() + "' existe déjà");
                        }
                    });
        }

        // Vérifier les prix avec BigDecimal
        if (request.getPrixAchat().compareTo(request.getPrixVente()) >= 0) {
            throw new IllegalArgumentException("Le prix de vente doit être supérieur au prix d'achat");
        }

        // Mettre à jour les champs non sensibles
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setCategorie(request.getCategorie());
        produit.setPrixVente(request.getPrixVente());
        produit.setPoids(request.getPoids());
        produit.setUnite(request.getUnite());
        produit.setActif(request.getActif());

        // Mettre à jour le prix d'achat chiffré (seulement si ADMIN)
        if ("ADMIN".equals(role)) {
            produit.setPrixAchatChiffre(encryptionUtil.encryptBigDecimal(request.getPrixAchat()));

            // Recalculer la marge
            BigDecimal marge = calculerMarge(request.getPrixVente(), request.getPrixAchat());
            produit.setMargeChiffree(encryptionUtil.encryptBigDecimal(marge));
        }

        Produit updatedProduit = produitRepository.save(produit);

        log.info("Produit mis à jour avec succès: ID {}", updatedProduit.getId());
        return createResponseForRole(updatedProduit, role);
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

    private BigDecimal calculerMarge(BigDecimal prixVente, BigDecimal prixAchat) {
        if (prixAchat.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Calcul: ((prixVente - prixAchat) / prixAchat) * 100
        BigDecimal difference = prixVente.subtract(prixAchat);
        BigDecimal marge = difference.divide(prixAchat, 4, RoundingMode.HALF_UP);
        return marge.multiply(new BigDecimal("100"));
    }

    private ProduitResponse createResponseForRole(Produit produit, String role) {
        ProduitResponse response = produitMapper.toResponse(produit);

        // Masquer les données sensibles pour les non-ADMIN
        if (!"ADMIN".equals(role)) {
            response.setPrixAchat(null);
            response.setMarge(null);
        } else {
            // Déchiffrer pour l'ADMIN
            try {
                response.setPrixAchat(encryptionUtil.decryptBigDecimal(produit.getPrixAchatChiffre()));
                response.setMarge(encryptionUtil.decryptBigDecimal(produit.getMargeChiffree()));
            } catch (Exception e) {
                log.error("Erreur lors du déchiffrement pour l'ADMIN", e);
                response.setPrixAchat(BigDecimal.ZERO);
                response.setMarge(BigDecimal.ZERO);
            }
        }

        return response;
    }

    // Méthodes simplifiées pour tests (sans gestion de rôle)
    @Override
    @Transactional(readOnly = true)
    public ProduitResponse getProduitById(Long id) {
        return getProduitById(id, "USER");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponse> getAllProduits() {
        return getAllProduits("USER");
    }
}