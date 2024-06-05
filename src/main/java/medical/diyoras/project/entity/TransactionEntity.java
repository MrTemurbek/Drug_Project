package medical.diyoras.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction", uniqueConstraints = {@UniqueConstraint(columnNames = {"drug_id", "point_id" })})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private double quantity;
    private double realPrice;
    private double salePrice;
    @ManyToOne(targetEntity = DrugEntity.class)
    @JoinColumn(name= "drug_id")
    private DrugEntity drug;
    @JoinColumn(name= "point_id")
    @ManyToOne(targetEntity = PointEntity.class)
    private PointEntity point;

}