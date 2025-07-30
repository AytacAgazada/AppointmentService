package com.example.appointmentservice.model.dto;

import com.example.appointmentservice.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDto {

    private Long customerId;

    @NotNull(message = "Business ID cannot be null")
    private Long businessId;

    @NotNull(message = "Appointment date cannot be null")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time cannot be null")
    private LocalTime appointmentTime;

    private Status status; // Client tərəfindən adətən təyin olunmaz, server tərəfindən PENDING olur

    private String note;
}