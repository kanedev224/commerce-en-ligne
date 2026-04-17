package sn.isi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.isi.dao.VenteRepository;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.VenteDTO;
import sn.isi.entities.VenteEntity;
import sn.isi.entities.ProduitEntity;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.exception.RequestException;
import sn.isi.mapper.VenteMapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenteServiceTest {
    @Mock
    private VenteRepository venteRepository;

    @Mock
    private ProduitRepository produitRepository;

    private VenteService venteService;
    private VenteMapper venteMapper;

    @BeforeEach
    void setUp() {
        venteMapper = Mappers.getMapper(VenteMapper.class);
        venteService = new VenteService(venteRepository, venteMapper, produitRepository);
    }

    @Test
    void testGetAllVentes() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        VenteEntity entity = VenteEntity.builder()
                .id(1L)
                .dateVente(new Date())
                .quantity(5.0)
                .produit(produit)
                .build();
        when(venteRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<VenteDTO> result = venteService.getAllVentes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(venteRepository, times(1)).findAll();
    }

    @Test
    void testGetVenteById_Success() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        VenteEntity entity = VenteEntity.builder()
                .id(1L)
                .dateVente(new Date())
                .quantity(5.0)
                .produit(produit)
                .build();
        when(venteRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        VenteDTO result = venteService.getVenteById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5.0, result.getQuantity());
        verify(venteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetVenteById_NotFound() {
        // Given
        when(venteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> venteService.getVenteById(1L));
        verify(venteRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateVente_Success() {
        // Given
        VenteDTO dto = VenteDTO.builder()
                .dateVente(new Date())
                .quantity(5.0)
                .produitId(1L)
                .build();

        ProduitEntity produit = ProduitEntity.builder()
                .id(1L)
                .name("Produit 1")
                .stock(100.0)
                .build();

        VenteEntity entity = VenteEntity.builder()
                .id(1L)
                .dateVente(new Date())
                .quantity(5.0)
                .produit(produit)
                .build();

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(venteRepository.save(any())).thenReturn(entity);

        // When
        VenteDTO result = venteService.createVente(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5.0, result.getQuantity());
        verify(produitRepository, times(1)).findById(1L);
        verify(venteRepository, times(1)).save(any());
    }

    @Test
    void testCreateVente_InsufficientStock() {
        // Given
        VenteDTO dto = VenteDTO.builder()
                .dateVente(new Date())
                .quantity(150.0)
                .produitId(1L)
                .build();

        ProduitEntity produit = ProduitEntity.builder()
                .id(1L)
                .name("Produit 1")
                .stock(100.0)
                .build();

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        // When & Then
        assertThrows(RequestException.class, () -> venteService.createVente(dto));
        verify(produitRepository, times(1)).findById(1L);
        verify(venteRepository, never()).save(any());
    }

    @Test
    void testGetVentesByProduitId() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        VenteEntity entity = VenteEntity.builder()
                .id(1L)
                .dateVente(new Date())
                .quantity(5.0)
                .produit(produit)
                .build();
        when(venteRepository.findByProduitId(1L)).thenReturn(List.of(entity));

        // When
        List<VenteDTO> result = venteService.getVentesByProduitId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(venteRepository, times(1)).findByProduitId(1L);
    }
}
