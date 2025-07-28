package ru.kubsau.practise.internetshop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;
import ru.kubsau.practise.internetshop.model.dto.UserRequestDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.UserMapper;
import ru.kubsau.practise.internetshop.services.user.UserService;
import ru.kubsau.practise.internetshop.util.Authenticator;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class SecurityController {
    UserService userService;
    UserMapper userMapper;
    Authenticator authenticator;

    @PostMapping("/auth/register")
    public ResponseDTO register(@RequestBody @Valid UserRequestDTO user, HttpServletRequest request) {
        userService.save(userMapper.fromDto(user));
        authenticator.doAuthentication(user, request);
        return ResponseDTOCreator.getResponseOK();
    }
}
