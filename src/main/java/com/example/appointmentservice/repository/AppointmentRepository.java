package com.example.appointmentservice.repository;

import com.example.appointmentservice.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; // List importu əlavə edildi
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //  bir müştəriyə aid bütün randevuları qaytarır
    List<Appointment> findAllByCustomerId(Long customerId);

    // Biznesə aid bütün randevuları qaytarır
    List<Appointment> findAllByBusinessId(Long businessId);

    Optional<Appointment> findByBusinessIdAndAppointmentDateAndAppointmentTime(Long businessId, LocalDate appointmentDate, LocalTime appointmentTime);

    // Bir tarixə aid bütün randevuları qaytarır
    List<Appointment> findAllByAppointmentDate(LocalDate appointmentDate);

    // Müştəri və biznesə aid bütün randevuları qaytarır (əgər birdən çox ola bilərsə)
    List<Appointment> findAllByCustomerIdAndBusinessId(Long customerId, Long businessId);

    boolean existsByBusinessIdAndAppointmentDateAndAppointmentTime(Long businessId, LocalDate appointmentDate, LocalTime appointmentTime);
}
