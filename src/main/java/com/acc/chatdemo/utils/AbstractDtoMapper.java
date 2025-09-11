package com.acc.chatdemo.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
    * Abstract class for mapping between Entity and DTO.
    * E - Entity type
    * D - DTO type
 */
public abstract class AbstractDtoMapper<E, D> {
    protected abstract D toDto(@NotNull final E entity);

    public Page<D> toDto(@NotNull final Page<E> entityPage) {
        return entityPage.map(this::toDto);
    }

    public List<D> toDto(@NotNull final Collection<E> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .toList();
    }
}
