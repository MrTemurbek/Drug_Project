package medical.diyoras.project.service;

import lombok.SneakyThrows;
import medical.diyoras.project.entity.DrugEntity;
import medical.diyoras.project.entity.PointEntity;
import medical.diyoras.project.repo.DrugRepo;
import medical.diyoras.project.repo.PointRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
public class MainService {
    @Autowired
    PointRepo pointRepository;
    @Autowired
    DrugRepo drugRepo;

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

            pointRepository.saveAll(entities);
        }
    }


    @SneakyThrows
    public void importDrugs(MultipartFile file) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        List<DrugEntity> entities = new ArrayList<>();
        Random random = new Random();
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
                    if (cellValue.isBlank()){
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
                entities.add(entity);
            }

            drugRepo.saveAll(entities);
        }
    }

}
