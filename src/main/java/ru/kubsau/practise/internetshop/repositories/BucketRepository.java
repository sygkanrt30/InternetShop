package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.entities.Bucket;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, String> {

    @Query(value = "select list_of_products from bucket where username = trim(:username)",
            nativeQuery = true)
    String getListOfProducts(String username);

    @Query(value = "select * from bucket where username = trim(:username)",
            nativeQuery = true)
    Optional<Bucket> getBucket(String username);

    @Query(value = "update bucket set list_of_products = :newArray where username = trim(:username)", nativeQuery = true)
    @Modifying
    void updateListOfProductsInBucket(String username, long[] newArray);

    @Modifying
    @Query(value = "insert into bucket(username) values (trim(:username))",
            nativeQuery = true)
    void saveBucket(String username);
}
