package sn.isi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.isi.entities.AppUserEntity;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Integer> {
    Optional<AppUserEntity> findByEmail(String email);
}
