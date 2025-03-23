package ru.otus.pro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants(innerTypeName = "BaseFields", level = AccessLevel.PUBLIC)
@Schema
public class BulletinListItemDTO {
    private long id;
    private long boardId;
    private String boardName;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
