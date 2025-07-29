package ru.kubsau.practise.internetshop.service.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.services.order.ExcelFileCreator;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ExcelFileCreatorTest {
    private ExcelFileCreator excelFileCreator;

    @BeforeEach
    public void init() {
        excelFileCreator = new ExcelFileCreator();
    }

    @Test
    @DisplayName("Тест на правильный сценарий")
    public void createOrderTest_CorrectOrder() {
        var products = new HashMap<>(Map.of(
                new ProductResponseDTO(1L, "milk", true, 100, 1, "It is a milk"), 2,
                new ProductResponseDTO(2L, "cake", false, 100, 100, "It is a cake"), 10,
                new ProductResponseDTO(3L, "sugar", false, 312, 1123120, "It is a sugar"), 1,
                new ProductResponseDTO(4L, "eggs", true, 100, 13200, "It is a eggs"), 12
        ));
        String username = "test";
        var fullPath = String.format("%s%s%s", "orders/order_", username, ".xlsx");

        excelFileCreator.createExcelDocument(products, username);

        var output = Paths.get(fullPath);
        Assertions.assertTrue(output.toFile().exists());
    }
}
