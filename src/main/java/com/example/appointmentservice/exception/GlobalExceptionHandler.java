package com.example.appointmentservice.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "RESOURCE_NOT_FOUND");
        log.warn("ResourceNotFoundException caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "RESOURCE_ALREADY_EXISTS");
        log.warn("ResourceAlreadyExistsException caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDetails> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "INVALID_INPUT");
        log.warn("InvalidInputException caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorDetails> handleExternalServiceException(ExternalServiceException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "EXTERNAL_SERVICE_ERROR");
        log.error("ExternalServiceException caught: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidAppointmentTimeException.class)
    public ResponseEntity<ErrorDetails> handleInvalidAppointmentTimeException(InvalidAppointmentTimeException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "INVALID_APPOINTMENT_TIME");
        log.warn("InvalidAppointmentTimeException caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAppointmentAlreadyExistsException(AppointmentAlreadyExistsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "APPOINTMENT_ALREADY_EXISTS");
        log.warn("AppointmentAlreadyExistsException caught: {}", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    // Spring-in @Valid annotasiyası ilə yaranan validasiya xətalarını idarə etmək üçün
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Validasiya uğursuz oldu", request.getDescription(false), "VALIDATION_ERROR", errors);
        log.warn("MethodArgumentNotValidException caught: {}", errors);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // FeignExceptionları idarə etmək. Bunları ExternalServiceException-a çevirməliyik,
    // lakin burada birbaşa da tuta bilərik ki, xarici servisin qaytardığı statusu yansıtsın.
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignException(FeignException ex, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.status()); // FeignException-dan HTTP statusunu almaq
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR; // Tapılmasa default dəyər
        }
        String errorDescription = request.getDescription(false);
        String errorCode = "FEIGN_CLIENT_ERROR_" + status.value(); // Daha spesifik kod

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Xarici servis xətası: " + ex.getMessage(), errorDescription, errorCode);
        log.error("FeignException caught (Status: {}): {}", status.value(), ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, status);
    }

    // Digər bütün gözlənilməyən xətaları idarə etmək üçün ümumi bir handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Daxili server xətası: " + ex.getMessage(), request.getDescription(false), "INTERNAL_SERVER_ERROR");
        log.error("Unhandled exception caught: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}