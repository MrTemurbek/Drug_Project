package medical.diyoras.project.repo;


import medical.diyoras.project.entity.DrugEntity;
import medical.diyoras.project.entity.PointEntity;
import medical.diyoras.project.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByDrugAndPointAndMonthAndYear(DrugEntity drugEntity, PointEntity pointEntity, int month, int year);

}
