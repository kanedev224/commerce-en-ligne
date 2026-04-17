package sn.isi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.isi.entities.AppRoleEntity;
import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRoleEntity, Integer> {
    Optional<AppRoleEntity> findByNom(String nom);
}
