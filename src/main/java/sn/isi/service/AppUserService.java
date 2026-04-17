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
import sn.isi.dao.AppUserRepository;
import sn.isi.dto.AppUserDTO;
import sn.isi.entities.AppUserEntity;
import sn.isi.mapper.AppUserMapper;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "users")
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    @Transactional(readOnly = true)
    public List<AppUserDTO> getAllUsers() {
        log.info("Retrieving all users");
        return appUserRepository.findAll()
                .stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public AppUserDTO getUserById(Integer id) {
        log.info("Retrieving user with id: {}", id);
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
                });
    }

    @CachePut(key = "#result.id")
    @Transactional
    public AppUserDTO createUser(AppUserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        appUserRepository.findByEmail(userDTO.getEmail())
                .ifPresent(u -> {
                    log.error("User already exists with email: {}", userDTO.getEmail());
                    throw new RequestException("Un utilisateur avec cet email existe déjà", HttpStatus.CONFLICT);
                });

        AppUserEntity entity = appUserMapper.toEntity(userDTO);
        AppUserEntity saved = appUserRepository.save(entity);
        log.info("User created successfully with id: {}", saved.getId());
        return appUserMapper.toDTO(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public AppUserDTO updateUser(Integer id, AppUserDTO userDTO) {
        log.info("Updating user with id: {}", id);

        return appUserRepository.findById(id)
                .map(existing -> {
                    existing.setNom(userDTO.getNom());
                    existing.setPrenom(userDTO.getPrenom());
                    existing.setEmail(userDTO.getEmail());
                    existing.setPassword(userDTO.getPassword());
                    existing.setEtat(userDTO.getEtat());
                    AppUserEntity updated = appUserRepository.save(existing);
                    log.info("User updated successfully with id: {}", id);
                    return appUserMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);

        if (!appUserRepository.existsById(id)) {
            log.error("User not found with id: {}", id);
            throw new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
        }

        try {
            appUserRepository.deleteById(id);
            log.info("User deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting user with id: {}", id, e);
            throw new RequestException("Erreur lors de la suppression de l'utilisateur", HttpStatus.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    public AppUserDTO getUserByEmail(String email) {
        log.info("Retrieving user with email: {}", email);
        return appUserRepository.findByEmail(email)
                .map(appUserMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new EntityNotFoundException("Utilisateur non trouvé avec l'email: " + email);
                });
    }
}
