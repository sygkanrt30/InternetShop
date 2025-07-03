package ru.kubsau.practise.internetshop.model.dto;


public record ProductResponseDTO(
        Long id,
        String name,
        boolean isAvailable,
        int count,
        int price,
        String description) {}