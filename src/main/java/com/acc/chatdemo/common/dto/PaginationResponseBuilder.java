package com.acc.chatdemo.common.dto;

import org.springframework.data.domain.Page;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginationResponseBuilder {
    /**
     * Builds a PaginationResponse from a Page object.
     *
     * @param page the Page object containing the data
     * @return a PaginationResponse containing the nodes, total count, page number, and size
     */
    public static <T> PaginationResponse<T> build(Page<T> page) {
        PaginationResponse<T> response = new PaginationResponse<>();
        response.setNodes(page.getContent());
        response.setTotalCount((int) page.getTotalElements());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        return response;
    }

    /**
     * Builds a PaginationResponse from a Page object with entity to DTO mapping.
     *
     * @param page the Page object containing the entity data
     * @param mapper the function to convert entity to DTO
     * @return a PaginationResponse containing the mapped DTOs, total count, page number, and size
     */
    public static <E, D> PaginationResponse<D> build(Page<E> page, Function<E, D> mapper) {
        PaginationResponse<D> response = new PaginationResponse<>();
        response.setNodes(page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList()));
        response.setTotalCount((int) page.getTotalElements());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        return response;
    }
}
