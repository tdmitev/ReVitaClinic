package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConsultationMedicationId implements Serializable {

    @Column(name = "consultation_id")
    private Integer consultationId;

    @Column(name = "medication_id")
    private Integer medicationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultationMedicationId)) return false;
        ConsultationMedicationId that = (ConsultationMedicationId) o;
        return Objects.equals(consultationId, that.consultationId) &&
                Objects.equals(medicationId, that.medicationId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(consultationId, medicationId);
    }
}