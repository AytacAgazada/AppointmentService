package com.example.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    private String dataType;
    private String data;
    private String name;

}
