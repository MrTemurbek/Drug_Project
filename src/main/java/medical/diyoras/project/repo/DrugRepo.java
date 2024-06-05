package medical.diyoras.project.repo;

import medical.diyoras.project.entity.DrugEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepo extends JpaRepository<DrugEntity, Long> {
    DrugEntity getDrugEntitiesByMaterial(Long materialId);
}
