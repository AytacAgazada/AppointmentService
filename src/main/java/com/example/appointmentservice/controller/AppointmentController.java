package com.example.appointmentservice.controller;

import com.example.appointmentservice.model.dto.AppointmentRequestDto;
import com.example.appointmentservice.model.dto.AppointmentResponseDto;
import com.example.appointmentservice.model.enumeration.Status;
import com.example.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody AppointmentResponseDto appointmentRequestDto) {
        log.info("AppointmentController.createAppointment: Request received for customerId: {} and businessId: {}", appointmentRequestDto.getCustomerId(), appointmentRequestDto.getBusinessId());
        AppointmentResponseDto createdAppointment = appointmentService.createAppointment(appointmentRequestDto);
        log.info("Appointment created successfully with ID: {}", createdAppointment.getId());
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        log.info("AppointmentController.getAllAppointments: Fetching all appointments.");
        List<AppointmentResponseDto> appointments = appointmentService.getAllAppointments();
        log.info("Fetched {} appointments.", appointments.size());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        log.info("AppointmentController.getAppointmentById: Fetching appointment with ID: {}", id);
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);
        log.info("Appointment with ID: {} fetched successfully.", id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentByCustomerId(@PathVariable Long customerId) {
        log.info("AppointmentController.getAppointmentByCustomerId: Fetching appointment for customerId: {}", customerId);
        AppointmentResponseDto appointment = appointmentService.getAppointmentByCustomerId(customerId);
        log.info("Appointment for customerId: {} fetched successfully.", customerId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentByBusinessId(@PathVariable Long businessId) {
        log.info("AppointmentController.getAppointmentByBusinessId: Fetching appointment for businessId: {}", businessId);
        AppointmentResponseDto appointment = appointmentService.getAppointmentByBusinessId(businessId);
        log.info("Appointment for businessId: {} fetched successfully.", businessId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/customer/{customerId}/business/{businessId}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentByCustomerIdAndBusinessId(
            @PathVariable Long customerId,
            @PathVariable Long businessId) {
        log.info("AppointmentController.getAppointmentByCustomerIdAndBusinessId: Fetching appointment for customerId: {} and businessId: {}", customerId, businessId);
        AppointmentResponseDto appointment = appointmentService.getAppointmentByCustomerIdAndBusinessId(customerId, businessId);
        log.info("Appointment for customerId: {} and businessId: {} fetched successfully.", customerId, businessId);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        log.info("AppointmentController.updateAppointment: Request to update appointment ID: {}", id);
        AppointmentResponseDto updatedAppointment = appointmentService.updateAppointment(id, appointmentRequestDto);
        log.info("Appointment ID: {} updated successfully.", id);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody Status status) {
        log.info("AppointmentController.updateAppointmentStatus: Request to update status for appointment ID: {} to {}", id, status);
        AppointmentResponseDto updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        log.info("Appointment ID: {} status updated successfully to {}.", id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentID(@PathVariable Long id) {
        log.info("AppointmentController.deleteAppointment: Request to delete appointment ID: {}", id);
        appointmentService.deleteAppointmentId(id);
        log.info("Appointment ID: {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<Void> deleteAppointment(){
        log.info("AppointmentController.deleteAppointment: Request to delete all appointments.");
        appointmentService.deleteAppointment();
        log.info("All appointments deleted successfully.");
        return ResponseEntity.noContent().build();
    }

}
