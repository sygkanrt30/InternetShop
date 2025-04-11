package ru.kubsau.practise.internetshop.repositories.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "delete from product where name = :name", nativeQuery = true)
    Optional<Product> deleteProductByName(String name);

    @Modifying
    @Query(value = " update product set name = :name," +
            " is_suitable = :isSuitable," +
            " count = :count," +
            " price = :price," +
            " description = :description" +
            " where id = :id", nativeQuery = true)
    void updateProduct(long id,
                       @NonNull String name,
                       boolean isSuitable,
                       int count,
                       int price,
                       String description);

    @Modifying
    @Query(value = "update product set price = :price where id = :id", nativeQuery = true)
    void updatePrice(long id, double price);
}