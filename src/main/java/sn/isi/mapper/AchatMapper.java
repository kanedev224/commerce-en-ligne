package sn.isi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.isi.entities.AchatEntity;
import sn.isi.dto.AchatDTO;

@Mapper(componentModel = "spring")
public interface AchatMapper {
    @Mapping(target = "produitId", source = "produit.id")
    AchatDTO toDTO(AchatEntity entity);

    @Mapping(target = "produit", ignore = true)
    AchatEntity toEntity(AchatDTO dto);
}
