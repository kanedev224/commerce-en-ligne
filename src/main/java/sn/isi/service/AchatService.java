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
import sn.isi.dao.AchatRepository;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.AchatDTO;
import sn.isi.entities.AchatEntity;
import sn.isi.entities.ProduitEntity;
import sn.isi.mapper.AchatMapper;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "achats")
public class AchatService {
    private final AchatRepository achatRepository;
    private final AchatMapper achatMapper;
    private final ProduitRepository produitRepository;

    @Transactional(readOnly = true)
    public List<AchatDTO> getAllAchats() {
        log.info("Retrieving all achats");
        return achatRepository.findAll()
                .stream()
                .map(achatMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public AchatDTO getAchatById(Long id) {
        log.info("Retrieving achat with id: {}", id);
        return achatRepository.findById(id)
                .map(achatMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Achat not found with id: {}", id);
                    return new EntityNotFoundException("Achat non trouvé avec l'ID: " + id);
                });
    }

    @CachePut(key = "#result.id")
    @Transactional
    public AchatDTO createAchat(AchatDTO achatDTO) {
        log.info("Creating new achat for product id: {}", achatDTO.getProduitId());

        ProduitEntity produit = produitRepository.findById(achatDTO.getProduitId())
                .orElseThrow(() -> {
                    log.error("Produit not found with id: {}", achatDTO.getProduitId());
                    return new EntityNotFoundException("Produit non trouvé avec l'ID: " + achatDTO.getProduitId());
                });

        AchatEntity entity = achatMapper.toEntity(achatDTO);
        entity.setProduit(produit);
        AchatEntity saved = achatRepository.save(entity);
        log.info("Achat created successfully with id: {}", saved.getId());
        return achatMapper.toDTO(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public AchatDTO updateAchat(Long id, AchatDTO achatDTO) {
        log.info("Updating achat with id: {}", id);

        return achatRepository.findById(id)
                .map(existing -> {
                    existing.setDateAchat(achatDTO.getDateAchat());
                    existing.setQuantity(achatDTO.getQuantity());

                    if (!existing.getProduit().getId().equals(achatDTO.getProduitId())) {
                        ProduitEntity produit = produitRepository.findById(achatDTO.getProduitId())
                                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé"));
                        existing.setProduit(produit);
                    }

                    AchatEntity updated = achatRepository.save(existing);
                    log.info("Achat updated successfully with id: {}", id);
                    return achatMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("Achat not found with id: {}", id);
                    return new EntityNotFoundException("Achat non trouvé avec l'ID: " + id);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deleteAchat(Long id) {
        log.info("Deleting achat with id: {}", id);

        if (!achatRepository.existsById(id)) {
            log.error("Achat not found with id: {}", id);
            throw new EntityNotFoundException("Achat non trouvé avec l'ID: " + id);
        }

        try {
            achatRepository.deleteById(id);
            log.info("Achat deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting achat with id: {}", id, e);
            throw new RequestException("Erreur lors de la suppression de l'achat", HttpStatus.CONFLICT);
        }
    }

    public List<AchatDTO> getAchatsByProduitId(Long produitId) {
        log.info("Retrieving achats for product id: {}", produitId);
        return achatRepository.findByProduitId(produitId)
                .stream()
                .map(achatMapper::toDTO)
                .collect(Collectors.toList());
    }
}
