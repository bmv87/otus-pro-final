package ru.otus.pro.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class ErrorDTO {
    protected int code;
    protected String message;
    protected String stackTrace;
    protected LocalDateTime dateTime;
}