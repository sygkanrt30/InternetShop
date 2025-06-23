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
        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());
        user.setBucketOwner(new Bucket(userDTO.username()));
        return user;
    }
}
