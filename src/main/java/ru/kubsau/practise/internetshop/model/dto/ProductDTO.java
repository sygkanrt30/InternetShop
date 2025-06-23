package ru.kubsau.practise.internetshop.model.dto;

public record ProductDTO(Long id, String name, boolean isAvailable, int price, String description) {
}