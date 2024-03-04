package ru.sushchenko.supplier.util.mapper;

import org.springframework.stereotype.Component;

public interface Mapper<Entity, ResponseDto, RequestDto> {
    ResponseDto toDto(Entity entity);
    Entity toEntity(RequestDto dto);
}