package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sick_leave", schema = "revitaclinic")
public class SickLeave extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}