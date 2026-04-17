package sn.isi.mapper;

import org.mapstruct.Mapper;
import sn.isi.entities.AppUserEntity;
import sn.isi.dto.AppUserDTO;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO toDTO(AppUserEntity entity);
    
    AppUserEntity toEntity(AppUserDTO dto);
}
