package sn.isi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.isi.entities.ProduitEntity;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<ProduitEntity, Long> {
    Optional<ProduitEntity> findByRef(String ref);

    Optional<ProduitEntity> findByName(String name);
}
