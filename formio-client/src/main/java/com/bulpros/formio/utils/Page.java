package com.bulpros.formio.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    private Long totalPages;
    private Long totalElements;
    private List<T> elements;
}
