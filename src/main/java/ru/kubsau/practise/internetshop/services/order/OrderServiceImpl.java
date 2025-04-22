package ru.kubsau.practise.internetshop.services.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    static String FILE_PATH = "src/main/resources/orders/order_";
    static String FILE_TYPE =  ".xlsx";
    BucketService bucketService;
    ProductAvailabilityTracker productAvailabilityTracker;

    @Override
    @Transactional
    public String createOrder(String username) {
        List<Product> products = bucketService.getProductsInBucket(username);
        throwIfListEmpty(products);
        Map<String, Long> map = convertListToMap(products);
        createXSSFWorkbook(map, username);
        removeGarbageDataInOtherTables(username, map);
        return "Order created successfully";
    }

    private void throwIfListEmpty(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalStateException("There are no products in bucket");
        }
    }

    private Map<String, Long> convertListToMap(List<Product> products) {
        return products.stream().collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
    }

    private void createXSSFWorkbook(Map<String, Long> map, String username) {
        try (var wb = new XSSFWorkbook()) {
            makeSheetWithData(wb, map, username);
            createXlsxFile(wb, username);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createXlsxFile(XSSFWorkbook file, String username) {
        try (var out = new FileOutputStream(FILE_PATH + username.trim() + FILE_TYPE)) {
            file.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void makeSheetWithData(XSSFWorkbook wb, Map<String, Long> map, String username) {
        var sheet = wb.createSheet(username);
        sheet.setDefaultColumnWidth(20);
        int row = 0;
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            formTableRow(sheet, entry, row);
            row++;
        }
    }

    private void formTableRow(XSSFSheet sheet, Map.Entry<String, Long> entry, int k) {
        var row = sheet.createRow(k);
        var cell1 = row.createCell(0);
        var cell2 = row.createCell(1);
        cell1.setCellValue(entry.getKey());
        cell2.setCellValue(entry.getValue());
    }

    private void removeGarbageDataInOtherTables(String username, Map<String, Long> map) {
        removeProductsFromDB(map);
        bucketService.clearAllProductsInBucket(username);
    }

    private void removeProductsFromDB(Map<String, Long> map) {
        map.forEach(productAvailabilityTracker::decrementProductCount);
    }
}
