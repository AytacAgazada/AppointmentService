package com.example.appointmentservice.exception;
//(Feign Client vasitəsilə digər mikroservislərlə əlaqədə problem yarandıqda.)
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Xarici servis problemi daxili server xətası kimi qiymətləndirilir
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}