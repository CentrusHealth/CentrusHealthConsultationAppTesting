package com.consultationapp.demo.repository;
import com.consultationapp.demo.entity.Consultation;
import com.consultationapp.demo.entity.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepository
        extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByClinicianIdAndStatus(
            UUID clinicianId,
            ConsultationStatus status
    );

    List<Consultation> findByPatientId(UUID patientId);
}