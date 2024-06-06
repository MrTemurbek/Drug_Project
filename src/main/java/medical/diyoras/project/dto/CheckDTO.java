package medical.diyoras.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import medical.diyoras.project.entity.TransactionEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckDTO {
    private Long material;
    private String pointCode;
    private int month;
    private int year;

    public CheckDTO(TransactionEntity transactionEntity) {
        this.material = transactionEntity.getDrug().getMaterial();
        this.pointCode = transactionEntity.getPoint().getCode();
        this.month = transactionEntity.getMonth();
        this.year = transactionEntity.getMonth();
    }
}
