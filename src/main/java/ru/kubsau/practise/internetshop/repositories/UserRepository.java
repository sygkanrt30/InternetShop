package ru.kubsau.practise.internetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kubsau.practise.internetshop.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value="select * from users where username = trim(:username)", nativeQuery=true)
    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "update user set username = trim(:newUsername) where username = trim(:oldUsername)",
            nativeQuery = true)
    void updateUsername(String newUsername, String oldUsername);
}
