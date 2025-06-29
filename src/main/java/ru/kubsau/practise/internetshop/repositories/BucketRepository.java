package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.model.entities.Bucket;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, String> {

    Optional<Bucket> findByUsername(String username);

    @Query("UPDATE Bucket b SET b.productIds = :newArray WHERE TRIM(b.username) = TRIM(:username)")
    @Modifying
    void updateBucket(String username, long[] newArray);
}
