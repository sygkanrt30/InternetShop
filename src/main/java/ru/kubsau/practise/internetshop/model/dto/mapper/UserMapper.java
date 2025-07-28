package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.UserRequestDTO;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;
import ru.kubsau.practise.internetshop.services.user.enums.Role;

import java.util.HashMap;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    default User fromDto(UserRequestDTO userDTO) {
        return new User(
                userDTO.username(),
                userDTO.email(),
                userDTO.password(),
                Role.USER,
                createDefaultBucket(userDTO.username())
        );
    }

    default Bucket createDefaultBucket(String username) {
        return new Bucket(username, new HashMap<>());
    }
}