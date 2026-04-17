package sn.isi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.isi.entities.VenteEntity;
import sn.isi.dto.VenteDTO;

@Mapper(componentModel = "spring")
public interface VenteMapper {
    @Mapping(target = "produitId", source = "produit.id")
    VenteDTO toDTO(VenteEntity entity);

    @Mapping(target = "produit", ignore = true)
    VenteEntity toEntity(VenteDTO dto);
}
