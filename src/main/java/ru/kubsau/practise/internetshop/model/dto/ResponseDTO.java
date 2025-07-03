package ru.kubsau.practise.internetshop.model.dto;

import org.springframework.http.HttpStatus;

public record ResponseDTO(HttpStatus status, String message) {
}
