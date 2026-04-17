package sn.isi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.isi.dao.AppRoleRepository;
import sn.isi.dto.AppRoleDTO;
import sn.isi.entities.AppRoleEntity;
import sn.isi.mapper.AppRoleMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppRolesServiceTest {

    @Mock
    private AppRoleRepository appRoleRepository;

    private AppRoleService appRoleService;
    private AppRoleMapper appRoleMapper;

    @BeforeEach
    void setUp() {
        appRoleMapper = Mappers.getMapper(AppRoleMapper.class);
        appRoleService = new AppRoleService(appRoleRepository, appRoleMapper);
    }

    @Test
    void testGetAllRoles() {

        AppRoleEntity entity = AppRoleEntity.builder()
                .id(1)
                .nom("ROLE_USER")
                .build();
        when(appRoleRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<AppRoleDTO> result = appRoleService.getAllRoles();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appRoleRepository, times(1)).findAll();
    }

    @Test
    void testGetRoleById_Success() {
        // Given
        AppRoleEntity entity = AppRoleEntity.builder()
                .id(1)
                .nom("ROLE_USER")
                .build();
        when(appRoleRepository.findById(1)).thenReturn(Optional.of(entity));

        // When
        AppRoleDTO result = appRoleService.getRoleById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("ROLE_USER", result.getNom());
        verify(appRoleRepository, times(1)).findById(1);
    }

    @Test
    void testCreateRole_Success() {
        // Given
        AppRoleDTO dto = AppRoleDTO.builder()
                .nom("ROLE_ADMIN")
                .build();

        AppRoleEntity entity = AppRoleEntity.builder()
                .id(1)
                .nom("ROLE_ADMIN")
                .build();

        when(appRoleRepository.save(any())).thenReturn(entity);

        // When
        AppRoleDTO result = appRoleService.createRole(dto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("ROLE_ADMIN", result.getNom());
        verify(appRoleRepository, times(1)).save(any());
    }

    @Test
    void testUpdateRole_Success() {
        // Given
        AppRoleDTO dto = AppRoleDTO.builder()
                .nom("ROLE_ADMIN_UPDATED")
                .build();

        AppRoleEntity existingEntity = AppRoleEntity.builder()
                .id(1)
                .nom("ROLE_ADMIN")
                .build();

        when(appRoleRepository.findById(1)).thenReturn(Optional.of(existingEntity));
        when(appRoleRepository.save(any())).thenReturn(existingEntity);

        // When
        AppRoleDTO result = appRoleService.updateRole(1, dto);

        // Then
        assertNotNull(result);
        verify(appRoleRepository, times(1)).findById(1);
        verify(appRoleRepository, times(1)).save(any());
    }

    @Test
    void testDeleteRole_Success() {
        // Given
        when(appRoleRepository.existsById(1)).thenReturn(true);

        // When
        appRoleService.deleteRole(1);

        // Then
        verify(appRoleRepository, times(1)).existsById(1);
        verify(appRoleRepository, times(1)).deleteById(1);
    }
}