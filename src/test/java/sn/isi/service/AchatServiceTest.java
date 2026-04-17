package sn.isi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.isi.dao.AchatRepository;
import sn.isi.dao.ProduitRepository;
import sn.isi.dto.AchatDTO;
import sn.isi.entities.AchatEntity;
import sn.isi.entities.ProduitEntity;
import sn.isi.exception.EntityNotFoundException;
import sn.isi.mapper.AchatMapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchatServiceTest {
    @Mock
    private AchatRepository achatRepository;

    @Mock
    private ProduitRepository produitRepository;

    private AchatService achatService;
    private AchatMapper achatMapper;

    @BeforeEach
    void setUp() {
        achatMapper = Mappers.getMapper(AchatMapper.class);
        achatService = new AchatService(achatRepository, achatMapper, produitRepository);
    }

    @Test
    void testGetAllAchats() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        AchatEntity entity = AchatEntity.builder()
                .id(1L)
                .dateAchat(new Date())
                .quantity(10.0)
                .produit(produit)
                .build();
        when(achatRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<AchatDTO> result = achatService.getAllAchats();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achatRepository, times(1)).findAll();
    }

    @Test
    void testGetAchatById_Success() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        AchatEntity entity = AchatEntity.builder()
                .id(1L)
                .dateAchat(new Date())
                .quantity(10.0)
                .produit(produit)
                .build();
        when(achatRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        AchatDTO result = achatService.getAchatById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10.0, result.getQuantity());
        verify(achatRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAchatById_NotFound() {
        // Given
        when(achatRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> achatService.getAchatById(1L));
        verify(achatRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateAchat_Success() {
        // Given
        AchatDTO dto = AchatDTO.builder()
                .dateAchat(new Date())
                .quantity(10.0)
                .produitId(1L)
                .build();

        ProduitEntity produit = ProduitEntity.builder()
                .id(1L)
                .name("Produit 1")
                .stock(100.0)
                .build();

        AchatEntity entity = AchatEntity.builder()
                .id(1L)
                .dateAchat(new Date())
                .quantity(10.0)
                .produit(produit)
                .build();

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(achatRepository.save(any())).thenReturn(entity);

        // When
        AchatDTO result = achatService.createAchat(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10.0, result.getQuantity());
        verify(produitRepository, times(1)).findById(1L);
        verify(achatRepository, times(1)).save(any());
    }

    @Test
    void testGetAchatsByProduitId() {
        // Given
        ProduitEntity produit = ProduitEntity.builder().id(1L).name("Produit 1").build();
        AchatEntity entity = AchatEntity.builder()
                .id(1L)
                .dateAchat(new Date())
                .quantity(10.0)
                .produit(produit)
                .build();
        when(achatRepository.findByProduitId(1L)).thenReturn(List.of(entity));

        // When
        List<AchatDTO> result = achatService.getAchatsByProduitId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achatRepository, times(1)).findByProduitId(1L);
    }
}
