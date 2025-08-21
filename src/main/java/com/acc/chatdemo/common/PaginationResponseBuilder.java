package com.acc.chatdemo.common;

import org.springframework.data.domain.Page;

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
}
