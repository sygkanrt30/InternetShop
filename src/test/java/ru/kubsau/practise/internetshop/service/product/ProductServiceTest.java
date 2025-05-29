package ru.kubsau.practise.internetshop.service.product;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;
import ru.kubsau.practise.internetshop.services.product.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    List<Product> products;
    Product product;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>(List.of(
                new Product(1L, "milk", true, 100, 1, "It is a milk"),
                new Product(2L, "cake", false, 100, 100, "It is a cake"),
                new Product(3L, "sugar", false, 312, 1123120, "It is a sugar"),
                new Product(4L, "eggs", true, 100, 13200, "It is a eggs"),
                new Product(5L, "bread", true, 10, 909870, "It is a bread")
        ));
        product = products.getFirst();
    }

    @Test
    @DisplayName("Проверяет верный ли список возвращает getAllProducts()")
    void getAllProductsCorrectCase() {
        List<Product> sortedProducts = new ArrayList<>(List.of(
                new Product(1L, "milk", true, 100, 1, "It is a milk"),
                new Product(4L, "eggs", true, 100, 13200, "It is a eggs"),
                new Product(5L, "bread", true, 10, 909870, "It is a bread"),
                new Product(2L, "cake", false, 100, 100, "It is a cake"),
                new Product(3L, "sugar", false, 312, 1123120, "It is a sugar")
        ));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertEquals(sortedProducts, result);
    }

    @Test
    @DisplayName("Сортируется ли список в getAllProducts()")
    void getAllProductsNotCorrectCase() {
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertNotEquals(products, result);
    }

    @Test
    @DisplayName("Корректнно ли работает getAllProducts() с пустым списком")
    void getAllProductsEmptyCase() {
        products.clear();
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Проверяет getProductById если id существует")
    void getProductByIdCorrectCase() {
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        Product result = productService.getById(product.getId());

        Assertions.assertEquals(product, result);
    }

    @Test
    @DisplayName("Проверяет getProductById если id не существует")
    void getProductByIdNotCorrectCase() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.getById(1L));
    }
}
