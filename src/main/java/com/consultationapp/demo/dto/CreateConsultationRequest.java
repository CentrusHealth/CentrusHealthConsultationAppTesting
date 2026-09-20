package com.consultationapp.demo.dto;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateConsultationRequest {
    private UUID patientId;
    private UUID clinicianId;
}
