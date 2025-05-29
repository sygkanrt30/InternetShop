package ru.kubsau.practise.internetshop.services.user;

import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.entities.User;

@Component
public class UserValidator {

    public static void validate(User userToValidate) {
        validateUsername(userToValidate);
        validateEmail(userToValidate);
        validatePassword(userToValidate);
    }

    private static void validateUsername(User user) {
        String username = user.getUsername();
        throwIfNotMatchesToRegex(username, Regex.REGEX_USERNAME.value());
    }

    private static void throwIfNotMatchesToRegex(String field, String regex) {
        if (!field.matches(regex) || field.length() < 5) {
            throw new InvalidRequestStateException("Invalid format!!!");
        }
    }

    private static void validateEmail(User user) {
        String email = user.getEmail();
        throwIfNotMatchesToRegex(email, Regex.REGEX_EMAIL.value());
    }

    private static void validatePassword(User user) {
        String password = user.getPassword();
        throwIfNotMatchesToRegex(password, Regex.REGEX_PASSWORD.value());
    }
}
