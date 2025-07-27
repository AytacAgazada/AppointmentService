package com.example.appointmentservice.model.dto;

import com.example.appointmentservice.model.entity.Appointment;
import com.example.appointmentservice.model.enumation.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDto {

    private Long id;
    private Long customerId;
    private Long businessId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Status status;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;



    public static AppointmentResponseDto fromEntity(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .customerId(appointment.getCustomerId())
                .businessId(appointment.getBusinessId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .note(appointment.getNote())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

}