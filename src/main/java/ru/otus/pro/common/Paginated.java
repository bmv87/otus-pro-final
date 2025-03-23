package ru.otus.pro.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Paginated<T> {
    private List<T> items;
    private long totalCount;
}
