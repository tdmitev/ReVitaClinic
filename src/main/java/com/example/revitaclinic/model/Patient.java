package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="patient", schema="revitaclinic")
public class Patient extends BaseEntity {

    @Column(name="kc_user_id", nullable=false, unique=true, updatable=false)
    private UUID keycloakUserId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kc_user_id", insertable=false, updatable=false)
    private AppUser user;

    @Column(name="egn", nullable=false, unique=true, length=10)
    private String egn;

    @Column(name="health_insurance_last_payment")
    private LocalDate healthInsuranceLastPayment;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="personal_doctor_id", nullable=false)
    private Doctor personalDoctor;

    public UUID getKeycloakUserId()         { return keycloakUserId; }
    public void setKeycloakUserId(UUID id)  { this.keycloakUserId = id; }

    public AppUser getUser()                { return user; }

    public String getEgn()                  { return egn; }
    public void setEgn(String e)            { this.egn = e; }

    public LocalDate getHealthInsuranceLastPayment() { return healthInsuranceLastPayment; }
    public void setHealthInsuranceLastPayment(LocalDate d) { this.healthInsuranceLastPayment = d; }

    public Doctor getPersonalDoctor()       { return personalDoctor; }
    public void setPersonalDoctor(Doctor d){ this.personalDoctor = d; }

    public void setUser(AppUser user) {
        this.user = user;
        this.keycloakUserId = user != null ? user.getKeycloakUserId() : this.keycloakUserId;
    }
}