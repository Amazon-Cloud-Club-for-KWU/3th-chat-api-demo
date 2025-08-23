package com.acc.chatdemo.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    private List<T> nodes;
    private int totalCount;
    private int page;
    private int size;
}
