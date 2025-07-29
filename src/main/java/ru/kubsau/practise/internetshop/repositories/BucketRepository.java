package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.model.entities.BucketEntity;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<BucketEntity, String> {
    Optional<BucketEntity> findByUsername(String username);
}
