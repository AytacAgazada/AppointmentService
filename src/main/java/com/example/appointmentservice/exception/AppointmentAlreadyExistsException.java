package com.example.appointmentservice.exception;
//(Eyni randevu vaxtı artıq tutulubsa, və ya digər unikal randevu məhdudiyyətləri üçün.)
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict - Resurs artıq mövcuddur
public class AppointmentAlreadyExistsException extends RuntimeException {
    public AppointmentAlreadyExistsException(String message) {
        super(message);
    }

    public AppointmentAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}