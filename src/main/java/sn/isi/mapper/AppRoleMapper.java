package sn.isi.mapper;

import org.mapstruct.Mapper;
import sn.isi.entities.AppRoleEntity;
import sn.isi.dto.AppRoleDTO;

@Mapper(componentModel = "spring")
public interface AppRoleMapper {
    AppRoleDTO toDTO(AppRoleEntity entity);

    AppRoleEntity toEntity(AppRoleDTO dto);
}
