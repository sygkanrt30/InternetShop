package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "update product set count = count - :countDecrement where id = :id",
            nativeQuery = true)
    @Modifying
    void decrementProductCount(long id, long countDecrement);

    @Query(value = "update product set is_available = false where id = :id",
            nativeQuery = true)
    @Modifying
    void makeFalseIsAvailableColumn(long id);

    @Query(value = "select * from product where name = trim(:nameOfProduct)",
            nativeQuery = true)
    Product getProductsByName(String nameOfProduct);
}