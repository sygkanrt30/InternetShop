package ru.kubsau.practise.internetshop.services.user;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.experimental.UtilityClass;
import ru.kubsau.practise.internetshop.model.entities.User;
import ru.kubsau.practise.internetshop.services.user.enums.Regex;

@UtilityClass
public class UserValidator {

    public void validate(User userToValidate) {
        validateUsername(userToValidate);
        validateEmail(userToValidate);
        validatePassword(userToValidate);
    }

    private static void validateUsername(User user) {
        String username = user.getUsername();
        throwIfNotMatchesToRegex(username, Regex.REGEX_USERNAME, 5);
    }

    private void throwIfNotMatchesToRegex(String field, Regex regex, int minLength) {
        if (!field.matches(regex.value())) {
            throw new InvalidRequestStateException("Invalid format of field: %s!!!".formatted(field));
        }
        if (field.length() < minLength){
            throw new InvalidRequestStateException("Invalid length of field: %s!!!".formatted(field));
        }
    }

    private void validateEmail(User user) {
        String email = user.getEmail();
        throwIfNotMatchesToRegex(email, Regex.REGEX_EMAIL, 10);
    }

    private void validatePassword(User user) {
        String password = user.getPassword();
        throwIfNotMatchesToRegex(password, Regex.REGEX_PASSWORD, 8);
    }
}
