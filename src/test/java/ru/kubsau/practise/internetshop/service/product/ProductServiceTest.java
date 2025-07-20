package ru.kubsau.practise.internetshop.service.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;
import ru.kubsau.practise.internetshop.services.product.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private Page<ProductResponseDTO> productPage;
    private List<ProductResponseDTO> sortedProducts;
    private List<Product> products;
    private Pageable pageable;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>(List.of(
                new Product(1L, "milk", true, 100, 1, "It is a milk"),
                new Product(2L, "cake", false, 100, 100, "It is a cake"),
                new Product(3L, "sugar", false, 312, 1123120, "It is a sugar"),
                new Product(4L, "eggs", true, 100, 13200, "It is a eggs"),
                new Product(5L, "bread", true, 10, 909870, "It is a bread")
        ));

        sortedProducts = new ArrayList<>(List.of(
                new ProductResponseDTO(1L, "milk", true, 100, 200, "It is a milk"),
                new ProductResponseDTO(4L, "eggs", true, 100, 200, "It is a eggs"),
                new ProductResponseDTO(5L, "bread", true, 10, 200, "It is a bread"),
                new ProductResponseDTO(2L, "cake", false, 100, 200, "It is a cake"),
                new ProductResponseDTO(3L, "sugar", false, 312, 200, "It is a sugar")
        ));

        pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "isAvailable"));
        productPage = new PageImpl<>(sortedProducts, pageable, products.size());
    }

    @Test
    @DisplayName("Проверяет верный ли список возвращает getAllProducts()")
    void getAllProductsCorrectCase() {
        Mockito.when(productRepository.findAllBy(ProductResponseDTO.class, pageable)).thenReturn(productPage);

        List<ProductResponseDTO> result = productService.getAll(0, 15);

        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertEquals(sortedProducts, result);
    }

    @Test
    @DisplayName("Сортируется ли список в getAllProducts()")
    void getAllProductsNotCorrectCase() {
        Mockito.when(productRepository.findAllBy(ProductResponseDTO.class, pageable)).thenReturn(productPage);

        List<ProductResponseDTO> result = productService.getAll(0, 15);

        Assertions.assertEquals(products.size(), result.size());
    }
}
