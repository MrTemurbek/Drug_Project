package medical.diyoras.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drugs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrugEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long material;
    private String materialName;
    private String manufacture;
    private String retailPrice;
    private String distributionPrice;
}
