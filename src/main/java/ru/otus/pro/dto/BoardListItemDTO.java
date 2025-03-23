package ru.otus.pro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants(innerTypeName = "BaseFields", level = AccessLevel.PUBLIC)
@Schema
public class BoardListItemDTO {
    private long id;
    private String name;
    private String description;
}
