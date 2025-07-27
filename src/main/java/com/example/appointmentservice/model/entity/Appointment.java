package com.example.appointmentservice.model.entity;

import com.example.appointmentservice.model.enumation.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Randevunu sifariş edən müştərinin ID-si
    @Column(nullable = false)
    private Long customerId;

    // Randevunun aid olduğu biznesin ID-si
    @Column(nullable = false)
    private Long businessId;

    // Tarix üçün LocalDate istifadə olunur
    @Column(nullable = false)
    private LocalDate appointmentDate;

    // Vaxt üçün LocalTime istifadə olunur
    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 500)
    private String note;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}