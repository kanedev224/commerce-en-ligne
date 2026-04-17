package sn.isi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.isi.entities.AchatEntity;
import java.util.List;

@Repository
public interface AchatRepository extends JpaRepository<AchatEntity, Long> {
    List<AchatEntity> findByProduitId(Long produitId);
}
