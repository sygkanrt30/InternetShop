package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.UserDTO;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    default User fromDto(UserDTO userDTO) {
        String username = userDTO.username();
        return User.builder()
                .username(username)
                .email(userDTO.email())
                .password(userDTO.password())
                .bucketOwner(new Bucket(username, new long[0]))
                .build();
    }
}