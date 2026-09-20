package com.consultationapp.demo.service;
import com.consultationapp.demo.dto.ConsultationResponse;
import com.consultationapp.demo.dto.CreateConsultationRequest;
import com.consultationapp.demo.entity.Consultation;
import com.consultationapp.demo.entity.ConsultationStatus;
import com.consultationapp.demo.entity.User;
import com.consultationapp.demo.entity.UserRole;
import com.consultationapp.demo.repository.ConsultationRepository;
import com.consultationapp.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ConsultationResponse createConsultation(
            CreateConsultationRequest request
    ) {

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        User clinician = userRepository.findById(request.getClinicianId())
                .orElseThrow(() ->
                        new RuntimeException("Clinician not found"));

        if (patient.getRole() != UserRole.PATIENT) {
            throw new RuntimeException("Selected user is not a patient");
        }

        if (clinician.getRole() != UserRole.CLINICIAN) {
            throw new RuntimeException("Selected user is not a clinician");
        }

        Consultation consultation = Consultation.builder()
                .patient(patient)
                .clinician(clinician)
                .status(ConsultationStatus.REQUESTED)
                .build();

        return mapToResponse(
                consultationRepository.save(consultation)
        );
    }

    public List<ConsultationResponse> getClinicianRequests(
            UUID clinicianId
    ) {

        return consultationRepository
                .findByClinicianIdAndStatus(
                        clinicianId,
                        ConsultationStatus.REQUESTED
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ConsultationResponse> getPatientConsultations(
            UUID patientId
    ) {

        return consultationRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ConsultationResponse joinConsultation(UUID consultationId) {

        Consultation consultation = consultationRepository
                .findById(consultationId)
                .orElseThrow(() ->
                        new RuntimeException("Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.REQUESTED) {
            consultation.setStatus(ConsultationStatus.IN_PROGRESS);
            consultation.setStartedAt(LocalDateTime.now());

            consultationRepository.save(consultation);
        }

        return mapToResponse(consultation);
    }

    private ConsultationResponse mapToResponse(
            Consultation consultation
    ) {

        User patient = consultation.getPatient();
        User clinician = consultation.getClinician();

        return ConsultationResponse.builder()
                .id(consultation.getId())
                .patientId(patient.getId())
                .patientName(
                        patient.getFirstName() + " " + patient.getLastName()
                )
                .clinicianId(clinician.getId())
                .clinicianName(
                        clinician.getFirstName() + " " + clinician.getLastName()
                )
                .status(consultation.getStatus())
                .createdAt(consultation.getCreatedAt())
                .startedAt(consultation.getStartedAt())
                .completedAt(consultation.getCompletedAt())
                .build();
    }
}