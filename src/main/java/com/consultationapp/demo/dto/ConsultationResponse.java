package com.consultationapp.demo.dto;
import com.consultationapp.demo.entity.ConsultationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponse {

    private UUID id;

    private UUID patientId;
    private String patientName;

    private UUID clinicianId;
    private String clinicianName;

    private ConsultationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}