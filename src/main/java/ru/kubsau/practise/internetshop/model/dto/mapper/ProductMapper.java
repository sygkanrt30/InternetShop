package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isAvailable", source = "available")
    ProductResponseDTO toDto(Product product);
}
