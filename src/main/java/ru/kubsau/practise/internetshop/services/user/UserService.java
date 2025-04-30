package ru.kubsau.practise.internetshop.services.user;

import ru.kubsau.practise.internetshop.entities.User;

public interface UserService {
    void save(User user);

    boolean isRegistered(String username, String password);
}
