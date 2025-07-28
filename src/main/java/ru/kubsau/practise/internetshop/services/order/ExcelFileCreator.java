package ru.kubsau.practise.internetshop.services.order;

import lombok.experimental.NonFinal;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class ExcelFileCreator {
    @Value("${excel.file.path}")
    @NonFinal String filePath;
    @Value("${excel.file.type}")
    @NonFinal String fileType;


    public void createXlsxFile(Map<ProductResponseDTO, Integer> map, String username) {
        try (var wb = new XSSFWorkbook()) {
            createExcelSheet(wb, map, username);
            createXlsxFile(wb, username);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createExcelSheet(XSSFWorkbook wb, Map<ProductResponseDTO, Integer> map, String username) {
        var sheet = wb.createSheet(username);
        sheet.setDefaultColumnWidth(20);
        int row = 0;
        for (var entry : map.entrySet()) {
            formTableRow(sheet, entry, row);
            row++;
        }
    }

    private void createXlsxFile(XSSFWorkbook file, String username) throws IOException {
        try (var out = new FileOutputStream(filePath + username.trim() + fileType)) {
            file.write(out);
        }
    }

    private void formTableRow(XSSFSheet sheet, Map.Entry<ProductResponseDTO, Integer> entry, int k) {
        var row = sheet.createRow(k);
        var cell1 = row.createCell(0);
        var cell2 = row.createCell(1);
        cell1.setCellValue(entry.getKey().name());
        cell2.setCellValue(entry.getValue());
    }
}
