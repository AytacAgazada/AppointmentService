package com.example.appointmentservice.exception;
//(Əgər eyni randevu artıq mövcuddursa istifadə oluna bilər,
// amma daha çox domen-spesifik AppointmentAlreadyExistsException daha uyğun olardı.)
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

