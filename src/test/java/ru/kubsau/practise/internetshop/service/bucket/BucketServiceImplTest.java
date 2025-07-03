package ru.kubsau.practise.internetshop.service.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketServiceImpl;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.*;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BucketServiceImplTest {
    @Mock
    private BucketRepository bucketRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductService productService;
    @InjectMocks
    private BucketServiceImpl bucketServiceImpl;

    @Test
    @DisplayName("1 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket1() {
        var productDto = new ProductResponseDTO(2L, "milk", true, 100, 1, "It is a milk");
        var product = new Product(2L, "milk", true, 100, 1, "It is a milk");
        var productsInBucket = new HashMap<>(Map.of(
                2L, 1
        ));

        var bucket = new Bucket("username", productsInBucket);

        Mockito.when(productService.getProductsAccordingToIds(Set.of(2L))).thenReturn(List.of(product));
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));
        Mockito.when(productMapper.toDto(product)).thenReturn(productDto);

        Map<ProductResponseDTO, Integer> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(productsInBucket.get(productDto.id()), result.get(productDto));
        Assertions.assertEquals(productsInBucket.size(), result.size());
    }

    @Test
    @DisplayName("2 getProductsInBucket(username) пустая мапа")
    public void getProductsInBucket2() {
        var emptyMap = new HashMap<Long, Integer>();
        var bucket = new Bucket("username", emptyMap);
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));
        Mockito.when(productService.getProductsAccordingToIds(Set.of())).thenReturn(List.of());

        Map<ProductResponseDTO, Integer> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("1 clearBucket(String username) корректный сценарий")
    public void clearBucket1_CorrectCase() {
        var map = Map.of(
                2L, 1,
                3L, 4
        );
        var emptyMap = new HashMap<Long, Integer>();
        Bucket bucket = new Bucket("username", map);
        Bucket emptyBucket = new Bucket("username", emptyMap);

        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));
        Mockito.when(bucketRepository.save(emptyBucket)).thenThrow(
                new InvalidRequestStateException()
        );

        Assertions.assertThrows(InvalidRequestStateException.class, () -> bucketServiceImpl.clearBucket("username"));
    }

    @Test
    @DisplayName("2 clearBucket(String username) корректный сценарий")
    public void clearBucket2_CorrectCase() {
        var emptyMap = new HashMap<Long, Integer>();
        Bucket bucket = new Bucket("username", emptyMap);

        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.clearBucket("username"));
    }


    @ParameterizedTest(name = "removeAllProductsOfThisType(username, productId)  #{index}: Удаление из мапы {0} всех товаров с id {1}")
    @MethodSource("provideTestMapsForRemoveTypeOfProduct")
    public void removeAllProductsOfThisType_CorrectCase(Map<Long, Integer> map,
                                                        Map<Long, Integer> filteredMap,
                                                        long itemId) {
        var bucket = new Bucket("username", map);
        var filteredBucket = new Bucket("username", filteredMap);

        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(bucket));
        Mockito.when(bucketRepository.save(filteredBucket)).thenThrow(
                new InvalidRequestStateException()
        );

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", itemId));
    }

    private static Stream<Arguments> provideTestMapsForRemoveTypeOfProduct() {
        return Stream.of(
                Arguments.of(
                        new HashMap<>(Map.of(
                                1L, 2,
                                2L, 3,
                                3L, 1)),
                        new HashMap<>(Map.of(
                                1L, 2,
                                3L, 1)),
                        2L
                ),
                Arguments.of(
                        new HashMap<>(Map.of(
                                1L, 5)),
                        new HashMap<>(Map.of()),
                        1L

                ),
                Arguments.of(
                        new HashMap<>(Map.of(
                                13L, 23,
                                2L, 33,
                                1L, 1)),
                        new HashMap<>(Map.of(
                                13L, 23,
                                2L, 33,
                                1L, 1)),
                        14L
                )
        );
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) нет корзины с таким владельцем")
    public void removeAllProductsOfThisType_InvalidUsername() {
        Mockito.when(bucketRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @ParameterizedTest(name = "removeProduct(username, productId) #{index}: Удаление из мапы {0} одного товара с id {1}")
    @MethodSource("provideTestMapsForRemoveProduct")
    public void removeProduct_CorrectCase(Map<Long, Integer> map,
                                          Map<Long, Integer> filteredMap,
                                          long itemId) {
        var bucket = new Bucket("username", map);
        var filteredBucket = new Bucket("username", filteredMap);

        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(bucket));
        Mockito.when(bucketRepository.save(filteredBucket)).thenThrow(
                new InvalidRequestStateException()
        );

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeProduct("username", itemId));
    }

    private static Stream<Arguments> provideTestMapsForRemoveProduct() {
        return Stream.of(
                Arguments.of(
                        new HashMap<>(Map.of(
                                1L, 2,
                                2L, 2,
                                3L, 1)),
                        new HashMap<>(Map.of(
                                1L, 2,
                                3L, 1)),
                        2L
                ),
                Arguments.of(
                        new HashMap<>(Map.of(
                                1L, 4)),
                        new HashMap<>(Map.of()),
                        1L

                ),
                Arguments.of(
                        new HashMap<>(Map.of(
                                13L, 23,
                                2L, 33,
                                1L, 1)),
                        new HashMap<>(Map.of(
                                13L, 23,
                                2L, 33,
                                1L, 1)),
                        14L
                )
        );
    }

    @Test
    @DisplayName("removeProduct(username, productId) нет корзины с таким владельцем")
    public void removeProduct_InvalidUsername() {
        Mockito.when(bucketRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("addProductsToBucket(username, productId) нет корзины с таким владельцем")
    public void addProductsToBucket_InvalidUsername() {
        Mockito.when(bucketRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }
}
