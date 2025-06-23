package ru.kubsau.practise.internetshop.model.dto;

import ru.kubsau.practise.internetshop.model.entities.Bucket;

public record UserDTO(String username, String email, String password, Bucket bucket) {
}
