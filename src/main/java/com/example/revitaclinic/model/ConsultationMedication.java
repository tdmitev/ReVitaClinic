package com.example.revitaclinic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultation_medication", schema = "revitaclinic")
public class ConsultationMedication {

    @EmbeddedId
    private ConsultationMedicationId id;

    @MapsId("consultationId")
    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @MapsId("medicationId")
    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(length = 255)
    private String dosage;

    @Column(length = 255)
    private String frequency;

    @Column(length = 255)
    private String duration;

    public ConsultationMedicationId getId() {
        return id;
    }

    public void setId(ConsultationMedicationId id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}