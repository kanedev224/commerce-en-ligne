package sn.isi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.ProduitDTO;
import sn.isi.entities.ProduitEntity;
import sn.isi.mapper.ProduitMapper;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "produits")
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    @Transactional(readOnly = true)
    public List<ProduitDTO> getAllProduits() {
        log.info("Retrieving all produits");
        return produitRepository.findAll()
                .stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public ProduitDTO getProduitById(Long id) {
        log.info("Retrieving produit with id: {}", id);
        return produitRepository.findById(id)
                .map(produitMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Produit not found with id: {}", id);
                    return new EntityNotFoundException("Produit non trouvé avec l'ID: " + id);
                });
    }

    @CachePut(key = "#result.id")
    @Transactional
    public ProduitDTO createProduit(ProduitDTO produitDTO) {
        log.info("Creating new produit with ref: {}", produitDTO.getRef());

        produitRepository.findByRef(produitDTO.getRef())
                .ifPresent(p -> {
                    log.error("Produit with ref already exists: {}", produitDTO.getRef());
                    throw new RequestException("Un produit avec cette référence existe déjà", HttpStatus.CONFLICT);
                });

        ProduitEntity entity = produitMapper.toEntity(produitDTO);
        ProduitEntity saved = produitRepository.save(entity);
        log.info("Produit created successfully with id: {}", saved.getId());
        return produitMapper.toDTO(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public ProduitDTO updateProduit(Long id, ProduitDTO produitDTO) {
        log.info("Updating produit with id: {}", id);

        return produitRepository.findById(id)
                .map(existing -> {
                    existing.setName(produitDTO.getName());
                    existing.setStock(produitDTO.getStock());
                    existing.setRef(produitDTO.getRef());
                    ProduitEntity updated = produitRepository.save(existing);
                    log.info("Produit updated successfully with id: {}", id);
                    return produitMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("Produit not found with id: {}", id);
                    return new EntityNotFoundException("Produit non trouvé avec l'ID: " + id);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deleteProduit(Long id) {
        log.info("Deleting produit with id: {}", id);

        if (!produitRepository.existsById(id)) {
            log.error("Produit not found with id: {}", id);
            throw new EntityNotFoundException("Produit non trouvé avec l'ID: " + id);
        }

        try {
            produitRepository.deleteById(id);
            log.info("Produit deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting produit with id: {}", id, e);
            throw new RequestException("Erreur lors de la suppression du produit", HttpStatus.CONFLICT);
        }
    }
}
