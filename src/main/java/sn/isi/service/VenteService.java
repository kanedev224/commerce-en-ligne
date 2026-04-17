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
import sn.isi.dao.VenteRepository;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.VenteDTO;
import sn.isi.entities.VenteEntity;
import sn.isi.entities.ProduitEntity;
import sn.isi.mapper.VenteMapper;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "ventes")
public class VenteService {
    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;
    private final ProduitRepository produitRepository;

    @Transactional(readOnly = true)
    public List<VenteDTO> getAllVentes() {
        log.info("Retrieving all ventes");
        return venteRepository.findAll()
                .stream()
                .map(venteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public VenteDTO getVenteById(Long id) {
        log.info("Retrieving vente with id: {}", id);
        return venteRepository.findById(id)
                .map(venteMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Vente not found with id: {}", id);
                    return new EntityNotFoundException("Vente non trouvée avec l'ID: " + id);
                });
    }

    @CachePut(key = "#result.id")
    @Transactional
    public VenteDTO createVente(VenteDTO venteDTO) {
        log.info("Creating new vente for product id: {}", venteDTO.getProduitId());

        ProduitEntity produit = produitRepository.findById(venteDTO.getProduitId())
                .orElseThrow(() -> {
                    log.error("Produit not found with id: {}", venteDTO.getProduitId());
                    return new EntityNotFoundException("Produit non trouvé avec l'ID: " + venteDTO.getProduitId());
                });

        if (produit.getStock() < venteDTO.getQuantity()) {
            log.warn("Insufficient stock for product id: {}", venteDTO.getProduitId());
            throw new RequestException("Stock insuffisant pour ce produit", HttpStatus.BAD_REQUEST);
        }

        VenteEntity entity = venteMapper.toEntity(venteDTO);
        entity.setProduit(produit);
        produit.setStock(produit.getStock() - venteDTO.getQuantity());
        VenteEntity saved = venteRepository.save(entity);
        log.info("Vente created successfully with id: {}", saved.getId());
        return venteMapper.toDTO(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public VenteDTO updateVente(Long id, VenteDTO venteDTO) {
        log.info("Updating vente with id: {}", id);

        return venteRepository.findById(id)
                .map(existing -> {
                    existing.setDateVente(venteDTO.getDateVente());
                    existing.setQuantity(venteDTO.getQuantity());

                    if (!existing.getProduit().getId().equals(venteDTO.getProduitId())) {
                        ProduitEntity produit = produitRepository.findById(venteDTO.getProduitId())
                                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé"));
                        existing.setProduit(produit);
                    }

                    VenteEntity updated = venteRepository.save(existing);
                    log.info("Vente updated successfully with id: {}", id);
                    return venteMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("Vente not found with id: {}", id);
                    return new EntityNotFoundException("Vente non trouvée avec l'ID: " + id);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deleteVente(Long id) {
        log.info("Deleting vente with id: {}", id);

        if (!venteRepository.existsById(id)) {
            log.error("Vente not found with id: {}", id);
            throw new EntityNotFoundException("Vente non trouvée avec l'ID: " + id);
        }

        try {
            venteRepository.deleteById(id);
            log.info("Vente deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting vente with id: {}", id, e);
            throw new RequestException("Erreur lors de la suppression de la vente", HttpStatus.CONFLICT);
        }
    }

    public List<VenteDTO> getVentesByProduitId(Long produitId) {
        log.info("Retrieving ventes for product id: {}", produitId);
        return venteRepository.findByProduitId(produitId)
                .stream()
                .map(venteMapper::toDTO)
                .collect(Collectors.toList());
    }
}
