package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.UserRequestDTO;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;

import java.util.HashMap;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "bucket", expression = "java(createDefaultBucket(userDTO.username()))")
    User fromDto(UserRequestDTO userDTO);

    default Bucket createDefaultBucket(String username) {
        return new Bucket(username, new HashMap<>());
    }
}