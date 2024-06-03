package medical.diyoras.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_sale")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String code;
    private String location;
}
