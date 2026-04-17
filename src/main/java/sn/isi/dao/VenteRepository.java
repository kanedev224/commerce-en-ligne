package sn.isi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.isi.entities.VenteEntity;
import java.util.List;

@Repository
public interface VenteRepository extends JpaRepository<VenteEntity, Long> {
    List<VenteEntity> findByProduitId(Long produitId);
}
