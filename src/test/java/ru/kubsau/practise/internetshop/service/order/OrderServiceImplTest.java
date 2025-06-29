package ru.kubsau.practise.internetshop.service.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketServiceImpl;
import ru.kubsau.practise.internetshop.services.order.ExcelFileCreator;
import ru.kubsau.practise.internetshop.services.order.OrderServiceImpl;
import ru.kubsau.practise.internetshop.services.order.ProductAvailabilityTracker;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    @SuppressWarnings("UnusedDeclaration")
    private ProductAvailabilityTracker productAvailabilityTracker;
    @Mock
    private BucketServiceImpl bucketService;
    @SuppressWarnings("unused")
    @Mock
    private ExcelFileCreator excelFileCreator;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Тест на выброс ошибки из-за пустой мапы")
    public void createOrderTest_ThrowException() {
        String username = "test";
        Mockito.when(bucketService.getBucket("test")).thenReturn(new HashMap<>());

        Assertions.assertThrows(IllegalStateException.class, () -> orderService.createOrder(username));
    }

    @Test
    @DisplayName("Тест на правильный сценарий")
    public void createOrderTest_CorrectOrder() {
        Map<Product, Long> products = new HashMap<>(Map.of(
                new Product(1L, "milk", true, 100, 1, "It is a milk"), 2L,
                new Product(2L, "cake", false, 100, 100, "It is a cake"), 10L,
                new Product(3L, "sugar", false, 312, 1123120, "It is a sugar"), 1L,
                new Product(4L, "eggs", true, 100, 13200, "It is a eggs") , 12L
        ));
        String username = "test";
        var fullPath = String.format("%s%s%s", "src/main/resources/orders/order_", username, ".xlsx");
        Mockito.when(bucketService.getBucket("test")).thenReturn(products);

        orderService.createOrder(username);

        Assertions.assertDoesNotThrow(() -> orderService.createOrder(username));
        var output = Paths.get(fullPath);
        Assertions.assertTrue(output.toFile().exists());
    }
}
