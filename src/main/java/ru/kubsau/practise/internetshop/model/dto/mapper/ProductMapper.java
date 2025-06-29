package ru.kubsau.practise.internetshop.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kubsau.practise.internetshop.model.dto.ProductDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isAvailable", source = "available")
    ProductDTO toDto(Product product);
}
