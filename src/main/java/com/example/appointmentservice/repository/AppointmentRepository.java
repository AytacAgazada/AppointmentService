package com.example.appointmentservice.repository;


import com.example.appointmentservice.model.entity.Appointment;
import com.example.appointmentservice.model.enumation.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByCustomerId(Long customerId);

    Optional<Appointment> findByBusinessId(Long businessId);

    Optional<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    Optional<Appointment> findByCustomerIdAndBusinessId(Long customerId, Long businessId);

    boolean existsByBusinessIdAndAppointmentDateAndAppointmentTime(Long businessId, LocalDate appointmentDate, LocalTime appointmentTime);

    Optional<Appointment> findByStatus(Status status);

    Optional<Appointment> findByBusinessIdAndAppointmentDate(Long businessId, LocalDate appointmentDate);
    Optional<Appointment> findByBusinessIdAndAppointmentDateAndStatus(Long businessId, LocalDate appointmentDate, Status status);
}