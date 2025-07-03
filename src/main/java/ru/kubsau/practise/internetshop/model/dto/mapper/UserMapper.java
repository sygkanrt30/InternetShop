package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.UserRequestDTO;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;

import java.util.HashMap;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    default User fromDto(UserRequestDTO userDTO) {
        return User.builder()
                .username(userDTO.username())
                .email(userDTO.email())
                .password(userDTO.password())
                .bucket(createDefaultBucket(userDTO.username()))
                .build();
    }

    private Bucket createDefaultBucket(String username) {
        return Bucket.builder()
                .username(username)
                .products(new HashMap<>())
                .build();
    }
}