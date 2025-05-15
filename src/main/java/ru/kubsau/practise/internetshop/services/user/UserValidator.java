package ru.kubsau.practise.internetshop.services.user;

import lombok.experimental.NonFinal;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.services.exceptions.InvalidRequestException;

@Component
public class UserValidator {
    @NonFinal
    User user;

    public void validate(User userToValidate) {
        this.user = userToValidate;
        validateUsername();
        validateEmail();
        validatePassword();
    }

    private void validateUsername() {
        String username = user.getUsername();
        throwIfNotMatchesToRegex(username, Regex.REGEX_USERNAME.value());
    }

    private void throwIfNotMatchesToRegex(String field, String regex) {
        if (!field.matches(regex)) {
            throw new InvalidRequestException("Invalid format!!!");
        }
    }

    private void validateEmail() {
        String email = user.getEmail();
        throwIfNotMatchesToRegex(email, Regex.REGEX_EMAIL.value());
    }

    private void validatePassword() {
        String password = user.getPassword();
        throwIfNotMatchesToRegex(password, Regex.REGEX_PASSWORD.value());
    }
}
