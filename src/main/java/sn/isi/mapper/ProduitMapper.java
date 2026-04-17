package sn.isi.mapper;

import org.mapstruct.Mapper;
import sn.isi.entities.ProduitEntity;
import sn.isi.dto.ProduitDTO;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    ProduitDTO toDTO(ProduitEntity entity);

    ProduitEntity toEntity(ProduitDTO dto);
}
