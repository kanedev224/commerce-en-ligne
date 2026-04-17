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
import sn.isi.dao.AppRoleRepository;
import sn.isi.dto.AppRoleDTO;
import sn.isi.entities.AppRoleEntity;
import sn.isi.mapper.AppRoleMapper;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "roles")
public class AppRoleService {
    private final AppRoleRepository appRoleRepository;
    private final AppRoleMapper appRoleMapper;

    @Transactional(readOnly = true)
    public List<AppRoleDTO> getAllRoles() {
        log.info("Retrieving all roles");
        return appRoleRepository.findAll()
                .stream()
                .map(appRoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public AppRoleDTO getRoleById(Integer id) {
        log.info("Retrieving role with id: {}", id);
        return appRoleRepository.findById(id)
                .map(appRoleMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", id);
                    return new EntityNotFoundException("Rôle non trouvé avec l'ID: " + id);
                });
    }

    @CachePut(key = "#result.id")
    @Transactional
    public AppRoleDTO createRole(AppRoleDTO roleDTO) {
        log.info("Creating new role: {}", roleDTO.getNom());

        appRoleRepository.findByNom(roleDTO.getNom())
                .ifPresent(r -> {
                    log.error("Role already exists with nom: {}", roleDTO.getNom());
                    throw new RequestException("Un rôle avec ce nom existe déjà", HttpStatus.CONFLICT);
                });

        AppRoleEntity entity = appRoleMapper.toEntity(roleDTO);
        AppRoleEntity saved = appRoleRepository.save(entity);
        log.info("Role created successfully with id: {}", saved.getId());
        return appRoleMapper.toDTO(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public AppRoleDTO updateRole(Integer id, AppRoleDTO roleDTO) {
        log.info("Updating role with id: {}", id);

        return appRoleRepository.findById(id)
                .map(existing -> {
                    existing.setNom(roleDTO.getNom());
                    AppRoleEntity updated = appRoleRepository.save(existing);
                    log.info("Role updated successfully with id: {}", id);
                    return appRoleMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", id);
                    return new EntityNotFoundException("Rôle non trouvé avec l'ID: " + id);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deleteRole(Integer id) {
        log.info("Deleting role with id: {}", id);

        if (!appRoleRepository.existsById(id)) {
            log.error("Role not found with id: {}", id);
            throw new EntityNotFoundException("Rôle non trouvé avec l'ID: " + id);
        }

        try {
            appRoleRepository.deleteById(id);
            log.info("Role deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting role with id: {}", id, e);
            throw new RequestException("Erreur lors de la suppression du rôle", HttpStatus.CONFLICT);
        }
    }
}
