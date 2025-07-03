package ru.kubsau.practise.internetshop.service.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;
import ru.kubsau.practise.internetshop.services.order.ProductAvailabilityTracker;

@ExtendWith(MockitoExtension.class)
public class ProductAvailabilityTrackerTest {
    @Mock
    @SuppressWarnings("unused")
    private ProductRepository productRepository;
    @InjectMocks
    private ProductAvailabilityTracker productAvailabilityTracker;

    @Test
    @DisplayName("Тест на не выброс exception в decrementProductCount")
    public void decrementProductCount_NotThrowException() {
        var product = new ProductResponseDTO(1L, "milk", true, 1, 1, "It is a milk");
        long amountDecrement = 1;

        Assertions.assertDoesNotThrow(() -> productAvailabilityTracker.decrementCount(product, amountDecrement));
    }

    @Test
    @DisplayName("Тест на выброс exception в decrementProductCount")
    public void decrementProductCount_ThrowException() {
        var product = new ProductResponseDTO(1L, "milk", true, 29, 1, "It is a milk");
        long amountDecrement = 30;

        Assertions.assertThrows(IllegalStateException.class,
                () -> productAvailabilityTracker.decrementCount(product, amountDecrement));
    }
}
