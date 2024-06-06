package medical.diyoras.project.repo;

import medical.diyoras.project.dto.CheckDTO;
import medical.diyoras.project.entity.DrugEntity;
import medical.diyoras.project.entity.PointEntity;
import medical.diyoras.project.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
    TransactionEntity findByDrugAndPointAndMonthAndYear(DrugEntity drugEntity, PointEntity pointEntity, int month, int year);

}
