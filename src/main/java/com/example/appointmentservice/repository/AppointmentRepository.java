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

    // Artıq tək nəticə deyil, bir müştəriyə aid bütün randevuları qaytarır
    List<Appointment> findAllByCustomerId(Long customerId); // <-- DƏYİŞİKLİK BURADADIR

    // Biznesə aid bütün randevuları qaytarır
    List<Appointment> findAllByBusinessId(Long businessId); // <-- Bu da List olmalıdır

    Optional<Appointment> findByBusinessIdAndAppointmentDateAndAppointmentTime(Long businessId, LocalDate appointmentDate, LocalTime appointmentTime);

    // Bir tarixə aid bütün randevuları qaytarır
    List<Appointment> findAllByAppointmentDate(LocalDate appointmentDate); // <-- Bu da List olmalıdır

    // Müştəri və biznesə aid bütün randevuları qaytarır (əgər birdən çox ola bilərsə)
    List<Appointment> findAllByCustomerIdAndBusinessId(Long customerId, Long businessId); // <-- Bu da List olmalıdır

    boolean existsByBusinessIdAndAppointmentDateAndAppointmentTime(Long businessId, LocalDate appointmentDate, LocalTime appointmentTime);
}
