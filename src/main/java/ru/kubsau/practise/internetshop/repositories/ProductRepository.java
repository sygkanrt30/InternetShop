package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.model.entities.Product;

@Repository
public interface ProductRepository extends JpaRepositoryImplementation<Product, Long> {

    @Query(value = "update product set count = :newCount where id = :id", nativeQuery = true)
    @Modifying
    void updateCount(long id, long newCount);

    @Query(value = "update product set is_available = false where id = :id", nativeQuery = true)
    @Modifying
    void makeAvailableFalse(long id);
}