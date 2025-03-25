package ru.otus.pro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class BulletinUpdateDTO {

    @NotEmpty(message = "'Title' is required field")
    private String title;
    @NotEmpty(message = "'Content' is required field")
    private String content;
    @Email(message = "Invalid email format")
    private String email;
    private String site;
    @Pattern(regexp = "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$", message = "Invalid phone format")
    private String phone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
