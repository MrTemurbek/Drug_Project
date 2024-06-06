package medical.diyoras.project.service;

import lombok.SneakyThrows;
import medical.diyoras.project.dto.CheckDTO;
import medical.diyoras.project.entity.DrugEntity;
import medical.diyoras.project.entity.PointEntity;
import medical.diyoras.project.entity.TransactionEntity;
import medical.diyoras.project.repo.DrugRepo;
import medical.diyoras.project.repo.PointRepo;
import medical.diyoras.project.repo.TransactionRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MainService {
    @Autowired
    PointRepo pointRepo;
    @Autowired
    DrugRepo drugRepo;
    @Autowired
    TransactionRepo transactionRepo;

    final Calendar calendar = Calendar.getInstance();

    @SneakyThrows
    public void importPoints(MultipartFile file) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        List<PointEntity> entities = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                rows.next();
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                PointEntity entity = new PointEntity();
                int i = 0;
                for (Cell currentCell : currentRow) {


                    String cellValue = switch (currentCell.getCellType()) {
                        case STRING -> currentCell.getStringCellValue();
                        case NUMERIC -> String.valueOf(currentCell.getNumericCellValue());
                        default -> throw new RuntimeException("wrong");
                    };

                    if (i == 0) {
                        entity.setCode(cellValue);
                    } else {
                        entity.setLocation(cellValue);
                    }
                    i++;
                }
                entities.add(entity);
            }
            pointRepo.saveAll(entities);
        }
    }


    @SneakyThrows
    public void importDrugs(MultipartFile file) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        Set<DrugEntity> entities = new HashSet<>();
        Random random = new Random();
        int count = 0;
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                rows.next();
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                DrugEntity entity = new DrugEntity();
                int i = 0;
                boolean finish = false;
                for (Cell currentCell : currentRow) {


                    String cellValue = switch (currentCell.getCellType()) {
                        case STRING -> currentCell.getStringCellValue();
                        case NUMERIC -> String.valueOf(currentCell.getNumericCellValue());
                        default -> "";
                    };
                    if (cellValue.isBlank()) {
                        break;
                    }

                    if (i == 0) {
                        entity.setMaterial(Long.parseLong(cellValue));
                    } else if (i == 1) {
                        entity.setMaterialName(cellValue);
                    } else if (i == 2) {
                        entity.setManufacture(cellValue);
                    } else if (i == 3) {
                        entity.setDistributionPrice(cellValue);
                        double retail = Double.parseDouble(cellValue);
                        double min = 0.93;
                        double max = 0.97;
                        double randomDouble = min + (max - min) * random.nextDouble();
                        entity.setRetailPrice(String.valueOf(retail * randomDouble));
                    }

                    i++;
                }
                if (entities.add(entity)) {
                    drugRepo.save(entity);
                    count++;
                    System.out.println("count: " + count);
                }
            }
        }
    }

    @SneakyThrows
    public void importTransaction(MultipartFile file) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        Set<CheckDTO> entities = new HashSet<>();
        Random random = new Random();
        int count = 0;
        int finish = 0;
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                rows.next();
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                TransactionEntity entity = new TransactionEntity();
                int i = 0;
                for (Cell currentCell : currentRow) {
                    if (currentCell.getCellType() == CellType.BLANK) {
                        finish++;
                        break;
                    }
                    String cellValue = "";
                    if (!isDateCell(currentCell)) {
                        cellValue = switch (currentCell.getCellType()) {
                            case STRING -> currentCell.getStringCellValue();
                            case NUMERIC -> String.valueOf(currentCell.getNumericCellValue());
                            default -> "";
                    };

                    };
                    if (cellValue.isBlank() && !isDateCell(currentCell)) {
                        break;
                    }

                    if (i == 0) {
                        entity.setDrug(drugRepo.getDrugEntitiesByMaterial(Long.parseLong(cellValue)));
                    }
                    else if (i == 1) {
                        Optional<PointEntity> pointEntitiesByCode = pointRepo.getPointEntitiesByCode(cellValue);
                        if (pointEntitiesByCode.isPresent()) {
                            entity.setPoint(pointEntitiesByCode.get());
                        } else {
                            PointEntity point = pointRepo.save(new PointEntity(cellValue, "-1"));
                            entity.setPoint(point);
                        }
                    } else if (i == 2){
                        Date date = currentCell.getDateCellValue();
                        int year = getCalendar(date).get(Calendar.YEAR);
                        int month = getCalendar(date).get(Calendar.MONTH) + 1;
                        entity.setMonth(month);
                        entity.setYear(year);
                    } else if (i == 3) {
                        entity.setQuantity(Double.parseDouble(cellValue) *(-1));
                    } else if (i == 4) {
                        double realPrice = Double.parseDouble(cellValue)*(-1);
                        entity.setRealPrice(Math.round(realPrice));
                        double min = 1.10;
                        double max = 1.15;
                        double randomDouble = min + (max - min) * random.nextDouble();
                        entity.setSalePrice(Math.round(randomDouble * realPrice));
                    }

                    i++;
                }
                if (entities.add(new CheckDTO(entity.getDrug().getMaterial(), entity.getPoint().getCode(), entity.getMonth(), entity.getYear()))) {
                    transactionRepo.save(entity);
                    count++;
                    System.out.println("count: " + count);
                } else {
                    TransactionEntity byDrugAndPoint = transactionRepo.findByDrugAndPoint(entity.getDrug(), entity.getPoint());
                    byDrugAndPoint.setSalePrice(entity.getSalePrice() + byDrugAndPoint.getSalePrice());
                    byDrugAndPoint.setRealPrice(entity.getRealPrice() + byDrugAndPoint.getRealPrice());
                    transactionRepo.save(byDrugAndPoint);
                    System.out.println("count: " + count);
                }
                if (finish > 10) {
                    break;
                }
            }
        }
    }

    private static boolean isDateCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return false;
        }
        if (DateUtil.isCellDateFormatted(cell)) {
            return true;
        }
        return false;
    }

    private Calendar getCalendar(Date date) {
        calendar.setTime(date);
        return calendar;
    }
}
