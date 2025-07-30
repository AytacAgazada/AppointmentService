package com.example.appointmentservice.service;

import com.example.appointmentservice.exception.ExternalServiceException;
import com.example.appointmentservice.exception.ResourceNotFoundException;
import com.example.appointmentservice.exception.AppointmentAlreadyExistsException;
import com.example.appointmentservice.feign.BusinessServiceClient;
import com.example.appointmentservice.feign.CustomerServiceClient;
import com.example.appointmentservice.model.dto.AppointmentRequestDto;
import com.example.appointmentservice.model.dto.AppointmentResponseDto;
import com.example.appointmentservice.model.dto.businessDto.BusinessDto;
import com.example.appointmentservice.model.entity.Appointment;
import com.example.appointmentservice.model.enumeration.Status;
import com.example.appointmentservice.repository.AppointmentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


// Bu anotasiya IntelliJ IDEA-nın Feign clientlərinin avtomatik inyeksiyasını tanımaması səbəbindən yaranan xəbərdarlığı susdurur.
// Kodun işləməsinə təsir etmir, sadəcə IDE-nin bir xəbərdarlığıdır.
//@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerServiceClient customerServiceClient;
    private final BusinessServiceClient businessServiceClient;

    public AppointmentResponseDto createAppointment(AppointmentResponseDto appointmentRequestDto) {
        log.info("AppointmentService.createAppointment: Request received for customerId: {} and businessId: {}", appointmentRequestDto.getCustomerId(), appointmentRequestDto.getBusinessId());

        // 1. Müştərinin mövcudluğunu yoxla (Customer Service-dən gələn müştəri ID-si əsasında)
        try {
            ResponseEntity<Boolean> customerExistsResponse = customerServiceClient.doesCustomerExistByCustomerId(appointmentRequestDto.getCustomerId());
            if (customerExistsResponse == null || !customerExistsResponse.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(customerExistsResponse.getBody())) {
                log.warn("Customer not found for Auth User ID: {}", appointmentRequestDto.getCustomerId());
                throw new ResourceNotFoundException("Customer not found with ID: " + appointmentRequestDto.getCustomerId());
            }
        } catch (FeignException.NotFound e) {
            log.warn("Customer with Auth User ID {} not found in Auth Service (404 from Feign).", appointmentRequestDto.getCustomerId(), e);
            throw new ResourceNotFoundException("Customer not found with ID: " + appointmentRequestDto.getCustomerId() + " (not found in Auth Service)", e);
        } catch (FeignException e) {
            log.error("Error communicating with Customer Service for Auth User ID {}: {}", appointmentRequestDto.getCustomerId(), e.getMessage(), e);
            throw new ExternalServiceException("Error communicating with Customer Service: " + e.getMessage(), e);
        }

        // 2. Biznesin mövcudluğunu yoxla (biznes ID əsasında) və detallarını al
        BusinessDto businessDetails;
        try {
            // Əvvəlcə biznesin mövcudluğunu yoxlayın
            ResponseEntity<Boolean> businessExistsResponse = businessServiceClient.doesBusinessExistById(appointmentRequestDto.getBusinessId());
            if (businessExistsResponse == null || !businessExistsResponse.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(businessExistsResponse.getBody())) {
                log.warn("Business not found for ID: {}", appointmentRequestDto.getBusinessId());
                throw new ResourceNotFoundException("Business not found with ID: " + appointmentRequestDto.getBusinessId());
            }

            // Biznes mövcuddursa, detallarını alın
            ResponseEntity<BusinessDto> businessDetailsResponse = businessServiceClient.getBusinessById(appointmentRequestDto.getBusinessId());
            if (businessDetailsResponse == null || !businessDetailsResponse.getStatusCode().is2xxSuccessful() || businessDetailsResponse.getBody() == null) {
                log.warn("Business details not retrieved for ID: {}", appointmentRequestDto.getBusinessId());
                throw new ExternalServiceException("Business details could not be retrieved for ID: " + appointmentRequestDto.getBusinessId());
            }
            businessDetails = businessDetailsResponse.getBody();

        } catch (FeignException.NotFound e) {
            log.warn("Business with ID {} not found in Business Service (404 from Feign).", appointmentRequestDto.getBusinessId(), e);
            throw new ResourceNotFoundException("Business not found with ID: " + appointmentRequestDto.getBusinessId() + " (not found in Business Service)", e);
        } catch (FeignException e) {
            log.error("Error communicating with Business Service for ID {}: {}", appointmentRequestDto.getBusinessId(), e.getMessage(), e);
            throw new ExternalServiceException("Error communicating with Business Service: " + e.getMessage(), e);
        }

        // 3. Randevu vaxtının artıq tutulub-tutulmadığını yoxla
        if (appointmentRepository.existsByBusinessIdAndAppointmentDateAndAppointmentTime(
                appointmentRequestDto.getBusinessId(),
                appointmentRequestDto.getAppointmentDate(),
                appointmentRequestDto.getAppointmentTime())) {
            log.warn("Appointment slot already taken for Business ID: {} at {} on {}",
                    appointmentRequestDto.getBusinessId(), appointmentRequestDto.getAppointmentTime(), appointmentRequestDto.getAppointmentDate());
            throw new AppointmentAlreadyExistsException("The selected appointment slot is already taken.");
        }

        // 4. Randevunu yarat (Entity-yə çevirib verilənlər bazasına yaz)
        Appointment appointment = Appointment.builder()
                .customerId(appointmentRequestDto.getCustomerId())
                .businessId(appointmentRequestDto.getBusinessId())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .status(Status.PENDING) // Yeni randevu həmişə "PENDING" statusu ilə başlayır
                .note(appointmentRequestDto.getNote())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with ID: {}", savedAppointment.getId());

        // 5. Cavabı "Response DTO"-ya çevir və qaytar
        return AppointmentResponseDto.builder()
                .id(savedAppointment.getId())
                .customerId(savedAppointment.getCustomerId())
                .businessId(savedAppointment.getBusinessId())
                .appointmentDate(savedAppointment.getAppointmentDate())
                .appointmentTime(savedAppointment.getAppointmentTime())
                .status(savedAppointment.getStatus())
                .note(savedAppointment.getNote())
                .createdAt(savedAppointment.getCreatedAt())
                .updatedAt(savedAppointment.getUpdatedAt())
                .build();
    }

    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        log.info("AppointmentService.getAppointmentById: Request received for ID: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    public AppointmentResponseDto getAppointmentByCustomerId(Long customerId) {
        log.info("AppointmentService.getAppointmentByCustomerId: Request received for customerId: {}", customerId);
        Appointment appointment = appointmentRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment found for customer ID: " + customerId));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    public AppointmentResponseDto getAppointmentByBusinessId(Long businessId) {
        log.info("AppointmentService.getAppointmentByBusinessId: Request received for businessId: {}", businessId);
        Appointment appointment = appointmentRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment found for business ID: " + businessId));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    public AppointmentResponseDto getAppointmentByDate(LocalDate appointmentDate) {
        log.info("AppointmentService.getAppointmentByDate: Request received for date: {}", appointmentDate);
        Appointment appointment = appointmentRepository.findByAppointmentDate(appointmentDate)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment found for date: " + appointmentDate));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    public AppointmentResponseDto getAppointmentByCustomerIdAndBusinessId(Long customerId, Long businessId) {
        log.info("AppointmentService.getAppointmentByCustomerIdAndBusinessId: Request received for customerId: {} and businessId: {}", customerId, businessId);
        Appointment appointment = appointmentRepository.findByCustomerIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for customer ID: " + customerId + " and business ID: " + businessId));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        log.info("AppointmentService.getAllAppointments: Fetching all appointments.");
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            // Əgər heç bir randevu tapılmazsa boş bir siyahı qaytarılır, ResourceNotFoundException atılmır.
            // Çünki "bütün" randevuların olmaması bir xəta deyil, sadəcə boş bir nəticədir.
            log.info("No appointments found in the system.");
        }
        return appointments.stream()
                .map(AppointmentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public AppointmentResponseDto updateAppointment(Long appointmentId, AppointmentRequestDto updatedAppointmentDto) {
        log.info("AppointmentService.updateAppointment: Request to update appointment ID: {} with data: {}", appointmentId, updatedAppointmentDto);

        // Randevunun mövcudluğunu yoxla
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        // Əgər vaxt və ya tarix dəyişibsə, yeni vaxtın tutulub-tutulmadığını yoxla (cari randevunun özünü nəzərə almadan)
        if (!existingAppointment.getAppointmentDate().equals(updatedAppointmentDto.getAppointmentDate()) ||
                !existingAppointment.getAppointmentTime().equals(updatedAppointmentDto.getAppointmentTime())) {

            if (appointmentRepository.existsByBusinessIdAndAppointmentDateAndAppointmentTime(
                    updatedAppointmentDto.getBusinessId(),
                    updatedAppointmentDto.getAppointmentDate(),
                    updatedAppointmentDto.getAppointmentTime())) {
                log.warn("Cannot update appointment ID {}: new slot taken for Business ID: {} at {} on {}",
                        appointmentId, updatedAppointmentDto.getBusinessId(), updatedAppointmentDto.getAppointmentTime(), updatedAppointmentDto.getAppointmentDate());
                throw new AppointmentAlreadyExistsException("The updated appointment slot is already taken.");
            }
        }

        // Yeniləməni tətbiq et
        existingAppointment.setCustomerId(updatedAppointmentDto.getCustomerId());
        existingAppointment.setBusinessId(updatedAppointmentDto.getBusinessId());
        existingAppointment.setAppointmentDate(updatedAppointmentDto.getAppointmentDate());
        existingAppointment.setAppointmentTime(updatedAppointmentDto.getAppointmentTime());
        existingAppointment.setNote(updatedAppointmentDto.getNote());

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Appointment ID: {} updated successfully.", updatedAppointment.getId());

        return AppointmentResponseDto.fromEntity(updatedAppointment);
    }

    public AppointmentResponseDto updateAppointmentStatus(Long appointmentId, Status newStatus) {
        log.info("AppointmentService.updateAppointmentStatus: Request to update status for appointment ID: {} to {}", appointmentId, newStatus);
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        existingAppointment.setStatus(newStatus);
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Appointment ID: {} status updated to {}.", updatedAppointment.getId(), newStatus);
        return AppointmentResponseDto.fromEntity(updatedAppointment);
    }

    public void deleteAppointmentId(Long appointmentId) {
        log.info("AppointmentService.deleteAppointment: Request to delete appointment ID: {}", appointmentId);
        if (!appointmentRepository.existsById(appointmentId)) {
            log.warn("Attempted to delete non-existent appointment with ID: {}", appointmentId);
            throw new ResourceNotFoundException("Appointment not found with ID: " + appointmentId);
        }
        appointmentRepository.deleteById(appointmentId);
        log.info("Appointment ID: {} deleted successfully.", appointmentId);
    }

    public void deleteAppointment(){
        log.info("AppointmentService.deleteAppointment: Request to delete all appointments.");
        appointmentRepository.deleteAll();
        log.info("All appointments deleted successfully.");
    }

}