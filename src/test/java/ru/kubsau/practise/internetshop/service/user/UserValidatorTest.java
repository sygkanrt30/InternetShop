package ru.kubsau.practise.internetshop.service.user;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;
import ru.kubsau.practise.internetshop.services.user.UserValidator;

public class UserValidatorTest {
    @Test
    @DisplayName("Тест на корректный сценарий")
    public void validateTest_CorrectCase() {
        String username = "slava";
        String password = "Ss@159357";
        String email = "slava.vy.2006@gmail.com";
        User user = new User(username, email, password, null, new Bucket());

        Assertions.assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Тест на не корректное имя")
    public void validateTest_IncorrectUsername() {
        String username = "daw3523s!!!!";
        String password = "Ss@159357";
        String email = "slava.vy.2006@gmail.com";
        User user = new User(username, email, password, null, new Bucket());

        Assertions.assertThrows(InvalidRequestStateException.class, () -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Тест на не корректный email")
    public void validateTest_IncorrectEmail() {
        String username = "Mike34124";
        String password = "Ss@159357";
        String email = "slava.vy.2006@@gmail.com";
        User user = new User(username, email, password, null, new Bucket());

        Assertions.assertThrows(InvalidRequestStateException.class, () -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Тест на не корректный пароль")
    public void validateTest_IncorrectPassword() {
        String username = "Mike34124";
        String password = "Sssdwwdw@";
        String email = "vadim.rer.21313@mail.ru";
        User user = new User(username, email, password, null, new Bucket());

        Assertions.assertThrows(InvalidRequestStateException.class, () -> UserValidator.validate(user));
    }
}
