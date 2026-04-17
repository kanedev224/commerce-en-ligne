package sn.isi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.ProduitDTO;
import sn.isi.entities.ProduitEntity;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import sn.isi.mapper.ProduitMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {
    @Mock
    private ProduitRepository produitRepository;

    private ProduitService produitService;
    private ProduitMapper produitMapper;

    @BeforeEach
    void setUp() {
        produitMapper = Mappers.getMapper(ProduitMapper.class);
        produitService = new ProduitService(produitRepository, produitMapper);
    }

    @Test
    void testGetAllProduits() {
        // Given
        ProduitEntity entity = ProduitEntity.builder()
                .id(1L)
                .ref("REF001")
                .name("Produit 1")
                .stock(100.0)
                .build();
        when(produitRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<ProduitDTO> result = produitService.getAllProduits();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("REF001", result.get(0).getRef());
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    void testGetProduitById_Success() {
        // Given
        ProduitEntity entity = ProduitEntity.builder()
                .id(1L)
                .ref("REF001")
                .name("Produit 1")
                .stock(100.0)
                .build();
        when(produitRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        ProduitDTO result = produitService.getProduitById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("REF001", result.getRef());
        verify(produitRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProduitById_NotFound() {
        // Given
        when(produitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> produitService.getProduitById(1L));
        verify(produitRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProduit_Success() {
        // Given
        ProduitDTO dto = ProduitDTO.builder()
                .ref("REF001")
                .name("Produit 1")
                .stock(100.0)
                .build();

        ProduitEntity entity = ProduitEntity.builder()
                .id(1L)
                .ref("REF001")
                .name("Produit 1")
                .stock(100.0)
                .build();

        when(produitRepository.findByRef("REF001")).thenReturn(Optional.empty());
        when(produitRepository.save(any())).thenReturn(entity);

        // When
        ProduitDTO result = produitService.createProduit(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("REF001", result.getRef());
        verify(produitRepository, times(1)).findByRef("REF001");
        verify(produitRepository, times(1)).save(any());
    }

    @Test
    void testUpdateProduit_Success() {
        // Given
        ProduitDTO dto = ProduitDTO.builder()
                .ref("REF002")
                .name("Produit Updated")
                .stock(150.0)
                .build();

        ProduitEntity existingEntity = ProduitEntity.builder()
                .id(1L)
                .ref("REF001")
                .name("Produit 1")
                .stock(100.0)
                .build();

        when(produitRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(produitRepository.save(any())).thenReturn(existingEntity);

        // When
        ProduitDTO result = produitService.updateProduit(1L, dto);

        // Then
        assertNotNull(result);
        verify(produitRepository, times(1)).findById(1L);
        verify(produitRepository, times(1)).save(any());
    }

    @Test
    void testDeleteProduit_Success() {
        // Given
        when(produitRepository.existsById(1L)).thenReturn(true);

        // When
        produitService.deleteProduit(1L);

        // Then
        verify(produitRepository, times(1)).existsById(1L);
        verify(produitRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduit_NotFound() {
        // Given
        when(produitRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> produitService.deleteProduit(1L));
        verify(produitRepository, times(1)).existsById(1L);
        verify(produitRepository, never()).deleteById(anyLong());
    }
}
