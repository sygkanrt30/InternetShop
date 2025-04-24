package ru.kubsau.practise.internetshop.services.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum Regex {
    REGEX_USERNAME("^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9_]*$"),
    REGEX_EMAIL("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$"),
    REGEX_PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*[a-zA-Z].*$");

    String value;
}
