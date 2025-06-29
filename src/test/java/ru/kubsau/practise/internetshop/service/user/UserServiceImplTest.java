package ru.kubsau.practise.internetshop.service.user;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.User;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.security.config.SecurityConfig;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.services.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BucketService bucketService;
    private UserServiceImpl userServiceImpl;
    private User userForSave;

    @BeforeEach
    public void setUp() {
        PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();
        userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder, bucketService);
        userForSave = new User("slava", "slava.vy.2006@gmail.com", "Ss@159357", null, new Bucket());
    }

    @Test
    @DisplayName("Тест save(user) корректный сценарий")
    public void save_CorrectCase() {
        Assertions.assertDoesNotThrow(() -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username;№;!!", "w", "slava_dwd434#"})
    @DisplayName("Тест save(user) выброс ошибки некорректном имени")
    public void save_IncorrectUsername(String username) {
        userForSave.setUsername(username);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"slaa.23.@@mail.ru", "slra.vy_2566@gmail.com", "s2\\\"@gmail.com"})
    @DisplayName("Тест save(user) выброс ошибки некорректном email")
    public void save_IncorrectEmail(String email) {
        userForSave.setEmail(email);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"159357", "ssser@fes", "Ss124dwd2"})
    @DisplayName("Тест save(user) выброс ошибки некорректном пароля")
    public void save_IncorrectPassword(String password) {
        userForSave.setPassword(password);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @Test
    @DisplayName("Тест save(user) ошибка в методе взаимодействия с бд")
    public void save_IncorrectCase() {
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(InvalidRequestStateException.class);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }
}
