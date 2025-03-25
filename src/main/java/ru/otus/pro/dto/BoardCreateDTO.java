package ru.otus.pro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class BoardCreateDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private boolean disabled;
}
