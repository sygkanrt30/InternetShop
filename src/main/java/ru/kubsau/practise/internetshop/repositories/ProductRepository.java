package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.model.entities.Product;

@Repository
public interface ProductRepository extends JpaRepositoryImplementation<Product, Long> {
    @Modifying
    @Query("UPDATE Product p SET p.count = :newCount WHERE p.id = :id")
    void updateCount(long id, long newCount);

    @Modifying
    @Query("UPDATE Product p SET p.isAvailable = false WHERE p.id = :id")
    void makeAvailableFalse(long id);
}