package sn.isi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.isi.dao.AppUserRepository;
import sn.isi.dto.AppUserDTO;
import sn.isi.entities.AppUserEntity;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import sn.isi.mapper.AppUserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;

    private AppUserService appUserService;
    private AppUserMapper appUserMapper;

    @BeforeEach
    void setUp() {
        appUserMapper = Mappers.getMapper(AppUserMapper.class);
        appUserService = new AppUserService(appUserRepository, appUserMapper);
    }

    @Test
    void testGetAllUsers() {
        // Given
        AppUserEntity entity = AppUserEntity.builder()
                .id(1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .password("password123")
                .etat(1)
                .build();
        when(appUserRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<AppUserDTO> result = appUserService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("jean@example.com", result.get(0).getEmail());
        verify(appUserRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        // Given
        AppUserEntity entity = AppUserEntity.builder()
                .id(1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .password("password123")
                .etat(1)
                .build();
        when(appUserRepository.findById(1)).thenReturn(Optional.of(entity));

        // When
        AppUserDTO result = appUserService.getUserById(1);

        // Then
        assertNotNull(result);
        assertEquals("jean@example.com", result.getEmail());
        verify(appUserRepository, times(1)).findById(1);
    }

    @Test
    void testCreateUser_Success() {
        // Given
        AppUserDTO dto = AppUserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .password("password123")
                .etat(1)
                .build();

        AppUserEntity entity = AppUserEntity.builder()
                .id(1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .password("password123")
                .etat(1)
                .build();

        when(appUserRepository.findByEmail("jean@example.com")).thenReturn(Optional.empty());
        when(appUserRepository.save(any())).thenReturn(entity);

        // When
        AppUserDTO result = appUserService.createUser(dto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("jean@example.com", result.getEmail());
        verify(appUserRepository, times(1)).findByEmail("jean@example.com");
        verify(appUserRepository, times(1)).save(any());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        // Given
        AppUserDTO dto = AppUserDTO.builder()
                .email("jean@example.com")
                .build();

        AppUserEntity existing = AppUserEntity.builder()
                .id(1)
                .email("jean@example.com")
                .build();

        when(appUserRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(existing));

        // When & Then
        assertThrows(RequestException.class, () -> appUserService.createUser(dto));
        verify(appUserRepository, times(1)).findByEmail("jean@example.com");
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void testGetUserByEmail_Success() {
        // Given
        AppUserEntity entity = AppUserEntity.builder()
                .id(1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .password("password123")
                .etat(1)
                .build();
        when(appUserRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(entity));

        // When
        AppUserDTO result = appUserService.getUserByEmail("jean@example.com");

        // Then
        assertNotNull(result);
        assertEquals("jean@example.com", result.getEmail());
        verify(appUserRepository, times(1)).findByEmail("jean@example.com");
    }
}
