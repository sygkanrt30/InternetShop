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
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    static String FILE_PATH = "src/main/resources/orders/order_";
    static String FILE_TYPE = ".xlsx";
    BucketService bucketService;
    ProductAvailabilityTracker productAvailabilityTracker;

    @Override
    @Transactional
    public void createOrder(String username) {
        Map<Product, Long> products = bucketService.getProductsInBucket(username);
        throwIfMapEmpty(products);
        createXSSFWorkbook(products, username);
        removeGarbageDataInOtherTables(username, products);
        log.info("Order created successfully");
    }

    private void throwIfMapEmpty(Map<Product, Long> products) {
        if (products.isEmpty()) {
            throw new IllegalStateException("There are no products in bucket");
        }
    }

    private void createXSSFWorkbook(Map<Product, Long> map, String username) {
        try (var wb = new XSSFWorkbook()) {
            makeSheetWithData(wb, map, username);
            createXlsxFile(wb, username);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void makeSheetWithData(XSSFWorkbook wb, Map<Product, Long> map, String username) {
        var sheet = wb.createSheet(username);
        sheet.setDefaultColumnWidth(20);
        int row = 0;
        for (Map.Entry<Product, Long> entry : map.entrySet()) {
            formTableRow(sheet, entry, row);
            row++;
        }
    }

    private void formTableRow(XSSFSheet sheet, Map.Entry<Product, Long> entry, int k) {
        var row = sheet.createRow(k);
        var cell1 = row.createCell(0);
        var cell2 = row.createCell(1);
        cell1.setCellValue(entry.getKey().getName());
        cell2.setCellValue(entry.getValue());
    }

    private void createXlsxFile(XSSFWorkbook file, String username) throws IOException {
        try (var out = new FileOutputStream(FILE_PATH + username.trim() + FILE_TYPE)) {
            file.write(out);
        }
    }

    private void removeGarbageDataInOtherTables(String username, Map<Product, Long> map) {
        removeProductsFromDB(map);
        bucketService.clearBucket(username);
    }

    private void removeProductsFromDB(Map<Product, Long> map) {
        map.forEach(productAvailabilityTracker::decrementProductCount);
    }
}
