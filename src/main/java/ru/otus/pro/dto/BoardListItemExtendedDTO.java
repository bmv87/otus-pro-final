package ru.otus.pro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants(level = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = true)
@Schema
public class BoardListItemExtendedDTO extends BoardListItemDTO {
    private String creator;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean disabled;
}
