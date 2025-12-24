package com.stockguard.stockguard.service.impl;

import com.stockguard.stockguard.dto.request.EntrepotRequest;
import com.stockguard.stockguard.dto.response.EntrepotResponse;
import com.stockguard.stockguard.exception.ResourceNotFoundException;
import com.stockguard.stockguard.mapper.EntrepotMapper;
import com.stockguard.stockguard.model.Entrepot;
import com.stockguard.stockguard.repository.EntrepotRepository;
import com.stockguard.stockguard.service.EntrepotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrepotServiceImpl implements EntrepotService {

    private final EntrepotRepository entrepotRepository;
    private final EntrepotMapper entrepotMapper;

    @Override
    @Transactional
    public EntrepotResponse createEntrepot(EntrepotRequest request) {
        log.info("Création d'un nouvel entrepôt: {}", request.getNom());

        // Vérifier si l'entrepôt existe déjà
        entrepotRepository.findByNom(request.getNom())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Un entrepôt avec le nom '" + request.getNom() + "' existe déjà");
                });

        Entrepot entrepot = entrepotMapper.toEntity(request);
        Entrepot savedEntrepot = entrepotRepository.save(entrepot);

        log.info("Entrepôt créé avec succès: ID {}", savedEntrepot.getId());
        return entrepotMapper.toResponse(savedEntrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepotResponse getEntrepotById(Long id) {
        log.debug("Recherche de l'entrepôt avec ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + id));

        return entrepotMapper.toResponse(entrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotResponse> getAllEntrepots() {
        log.debug("Récupération de tous les entrepôts");

        return entrepotRepository.findAll().stream()
                .map(entrepotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotResponse> getEntrepotsActifs() {
        log.debug("Récupération des entrepôts actifs");

        return entrepotRepository.findByActifTrue().stream()
                .map(entrepotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EntrepotResponse updateEntrepot(Long id, EntrepotRequest request) {
        log.info("Mise à jour de l'entrepôt avec ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + id));

        // Vérifier si le nouveau nom n'est pas déjà utilisé par un autre entrepôt
        if (!entrepot.getNom().equals(request.getNom())) {
            entrepotRepository.findByNom(request.getNom())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalArgumentException("Un autre entrepôt avec le nom '" + request.getNom() + "' existe déjà");
                        }
                    });
        }

        entrepotMapper.updateEntity(request, entrepot);

        // Recalculer la capacité utilisée si nécessaire
        if (entrepot.getStocks() != null) {
            double nouvelleCapaciteUtilisee = entrepot.getStocks().stream()
                    .mapToDouble(s -> s.getVolumeOccupe())
                    .sum();
            entrepot.setCapaciteUtilisee(nouvelleCapaciteUtilisee);
            entrepot.calculerTauxRemplissage();
        }

        Entrepot updatedEntrepot = entrepotRepository.save(entrepot);

        log.info("Entrepôt mis à jour avec succès: ID {}", updatedEntrepot.getId());
        return entrepotMapper.toResponse(updatedEntrepot);
    }

    @Override
    @Transactional
    public void deleteEntrepot(Long id) {
        log.info("Suppression de l'entrepôt avec ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + id));

        // Vérifier si l'entrepôt a des stocks
        if (!entrepot.getStocks().isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer l'entrepôt car il a des stocks associés");
        }

        entrepotRepository.delete(entrepot);
        log.info("Entrepôt supprimé avec succès: ID {}", id);
    }

    @Override
    @Transactional
    public void desactiverEntrepot(Long id) {
        log.info("Désactivation de l'entrepôt avec ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + id));

        entrepot.setActif(false);
        entrepotRepository.save(entrepot);

        log.info("Entrepôt désactivé avec succès: ID {}", id);
    }

    @Override
    @Transactional
    public void activerEntrepot(Long id) {
        log.info("Activation de l'entrepôt avec ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + id));

        entrepot.setActif(true);
        entrepotRepository.save(entrepot);

        log.info("Entrepôt activé avec succès: ID {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotResponse> getEntrepotsByVille(String ville) {
        log.debug("Récupération des entrepôts par ville: {}", ville);

        return entrepotRepository.findByVille(ville).stream()
                .map(entrepotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotResponse> getEntrepotsWithAvailableCapacity(Double volume) {
        log.debug("Recherche d'entrepôts avec capacité disponible: {} m³", volume);

        return entrepotRepository.findWithAvailableCapacity(volume).stream()
                .map(entrepotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepotResponse getEntrepotByNom(String nom) {
        log.debug("Recherche de l'entrepôt par nom: {}", nom);

        Entrepot entrepot = entrepotRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec le nom: " + nom));

        return entrepotMapper.toResponse(entrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculerTauxRemplissage(Long entrepotId) {
        log.debug("Calcul du taux de remplissage pour l'entrepôt ID: {}", entrepotId);

        Entrepot entrepot = entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepôt non trouvé avec ID: " + entrepotId));

        return entrepot.getTauxRemplissage();
    }

    @Override
    @Transactional(readOnly = true)
    public long countEntrepotsActifs() {
        log.debug("Comptage des entrepôts actifs");

        return entrepotRepository.countActifs();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageTauxRemplissage() {
        log.debug("Calcul du taux de remplissage moyen");

        return entrepotRepository.findAverageTauxRemplissage();
    }
}