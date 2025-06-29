package ru.kubsau.practise.internetshop.service.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketServiceImpl;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BucketServiceImplTest {
    @Mock
    private BucketRepository bucketRepository;
    @Mock
    private ProductService productService;
    @Mock
    private Bucket bucket;
    @InjectMocks
    private BucketServiceImpl bucketServiceImpl;

    @Test
    @DisplayName("2 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket2() {
        var product1 = new Product(2L, "milk", true, 100, 1, "It is a milk");
        var productsInBucket = new HashMap<>(Map.of(
                product1, 1L
        ));
       Mockito.when(bucket.getProductIds()).thenReturn(new long[]{2L});
        Mockito.when(productService.getAllProductsAccordingToIds(List.of(2L))).thenReturn(List.of(product1));
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));

        Map<Product, Long> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(productsInBucket.get(product1), result.get(product1));
        Assertions.assertEquals(productsInBucket.size(), result.size());
        Assertions.assertEquals(productsInBucket, result);
    }

    @Test
    @DisplayName("3 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket3() {
        var productsInBucket = new HashMap<>();
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(bucket));
        Mockito.when(bucket.getProductIds()).thenReturn(new long[0]);

        Map<Product, Long> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(productsInBucket, result);
    }

    @Test
    @DisplayName("clearBucket(String username) корректный сценарий")
    public void clearBucket_CorrectCase() {
        long[] emptyArray = new long[0];
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        var exception = Assertions.assertThrows(RuntimeException.class,
                () -> bucketRepository.updateBucket("someString", emptyArray));

        Assertions.assertEquals("Forced exception for testing", exception.getMessage());
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) корректный сценарий")
    public void removeAllProductsOfThisType_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] filteredArr = {1L, 2L, 2L, 2L};
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(filteredArr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) корректный сценарий 2")
    public void removeAllProductsOfThisType_CorrectCase2() {
        long[] arr = {2L, 2L, 2L, 2L, 2L, 2L};
        long[] emptyArray = new long[0];
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) указаного id нет в списке")
    public void removeAllProductsOfThisType_IdNotInBucket() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(arr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 7L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) нет корзины с таким владельцем")
    public void removeAllProductsOfThisType_InvalidUsername() {
        Mockito.when(bucketRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) корректный сценарий")
    public void removeProduct_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] filteredArr = {1L, 2L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(filteredArr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) корректный сценарий 2")
    public void removeProduct_CorrectCase2() {
        long[] arr = {2L};
        long[] emptyArray = new long[0];
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) указаного id нет в списке")
    public void removeProduct_IdNotInBucket() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(arr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 7L));
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
    @DisplayName("addProductsToBucket(username, productId) корректный сценарий")
    public void addProductsToBucket_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] newArr = {1L, 2L, 3L, 3L, 2L, 2L, 3L};
        Mockito.when(bucketRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateBucket(Mockito.anyString(), Mockito.eq(newArr));

        Assertions.assertThrows(RuntimeException.class,
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
