package com.example.appointmentservice.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class BusinessDto {

    private Long id;
    private String companyName;
    private String businessType;
    private String description;
    private String website;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long ownerId;


}
