package com.example.appointmentservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Null dəyərləri JSON cavabında göstərmir
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String path; // endpoint yolu
    private String errorCode; // Custom error code, e.g., "NOT_FOUND", "VALIDATION_ERROR", "EXTERNAL_SERVICE_ERROR"
    private Map<String, String> errors; // Validasiya xətaları üçün (field -> message)

    // Ümumi konstruktor (validasiya xətaları olmayan hallar üçün)
    public ErrorDetails(LocalDateTime timestamp, String message, String path, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode;
    }
}