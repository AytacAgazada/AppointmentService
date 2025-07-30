package com.example.appointmentservice.model.dto;

import com.example.appointmentservice.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAppointmentStatusDto {

    @NotNull(message = "Status cannot be null")
    private Status status;
}