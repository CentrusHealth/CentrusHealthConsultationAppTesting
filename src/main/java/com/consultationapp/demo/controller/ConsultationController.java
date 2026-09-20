package com.consultationapp.demo.controller;
import com.consultationapp.demo.dto.ConsultationResponse;
import com.consultationapp.demo.dto.CreateConsultationRequest;
import com.consultationapp.demo.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationResponse createConsultation(
            @RequestBody CreateConsultationRequest request
    ) {
        return consultationService.createConsultation(request);
    }

    @GetMapping("/clinician/{clinicianId}/requests")
    public List<ConsultationResponse> getClinicianRequests(
            @PathVariable UUID clinicianId
    ) {
        return consultationService
                .getClinicianRequests(clinicianId);
    }

    @GetMapping("/patient/{patientId}")
    public List<ConsultationResponse> getPatientConsultations(
            @PathVariable UUID patientId
    ) {
        return consultationService
                .getPatientConsultations(patientId);
    }

    @PostMapping("/{consultationId}/join")
    public ConsultationResponse joinConsultation(
            @PathVariable UUID consultationId
    ) {
        return consultationService
                .joinConsultation(consultationId);
    }
}