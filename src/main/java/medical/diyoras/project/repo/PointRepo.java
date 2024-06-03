package medical.diyoras.project.repo;

import medical.diyoras.project.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepo extends JpaRepository<PointEntity, Long> {
}
