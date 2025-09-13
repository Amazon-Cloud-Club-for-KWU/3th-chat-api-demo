package com.acc.chatdemo.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

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

    public PagedModel<D> toDto(@NotNull final Page<E> entityPage) {
        Page<D> page =  entityPage.map(this::toDto);
        return new PagedModel<>(page);
    }

    public List<D> toDto(@NotNull final Collection<E> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .toList();
    }
}
