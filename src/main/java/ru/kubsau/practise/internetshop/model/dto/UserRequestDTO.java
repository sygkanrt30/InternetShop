package ru.kubsau.practise.internetshop.model.dto;

import javax.validation.constraints.Pattern;

public record UserRequestDTO(
        @Pattern(regexp = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9_]*$",
                message = "Incorrect username") String username,
        @Pattern(regexp = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$"
                , message = "Incorrect email") String email,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*[a-zA-Z].*$"
                , message = "Incorrect password") String password) {
}
