package com.example.appointmentservice.exception;
//(Sizdə artıq var idi, randevu vaxtı biznes qaydalarına uyğun gəlmədikdə.)
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Randevu vaxtı keçərsiz olduğu üçün 400 Bad Request
public class InvalidAppointmentTimeException extends RuntimeException {
    public InvalidAppointmentTimeException(String message) {
        super(message);
    }

    public InvalidAppointmentTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}