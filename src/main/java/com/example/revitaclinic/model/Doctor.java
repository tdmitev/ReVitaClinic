package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="doctor", schema="revitaclinic")
public class Doctor extends BaseEntity {

    @Column(name="kc_user_id", nullable=false, unique=true, updatable=false)
    private UUID keycloakUserId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kc_user_id", insertable=false, updatable=false)
    private AppUser user;

    @Column(name="unique_id", nullable=false, unique=true, length=50)
    private String uniqueId;

    @Column(name="is_personal", nullable=false)
    private boolean personal;

    @ManyToMany
    @JoinTable(
            name="doctor_specialty", schema="revitaclinic",
            joinColumns        = @JoinColumn(name="doctor_id"),
            inverseJoinColumns = @JoinColumn(name="specialty_id")
    )
    private Set<Specialty> specialties = new HashSet<>();

    public UUID getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(UUID id) { this.keycloakUserId = id; }

    public AppUser getUser() { return user; }

    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String u) { this.uniqueId = u; }

    public boolean isPersonal() { return personal; }
    public void setPersonal(boolean p) { this.personal = p; }

    public Set<Specialty> getSpecialties() { return specialties; }
    public void setSpecialties(Set<Specialty> s) { this.specialties = s; }

    public void setUser(AppUser user) {
        this.user = user;
        this.keycloakUserId = user != null
                ? user.getKeycloakUserId()
                : this.keycloakUserId;
    }
}