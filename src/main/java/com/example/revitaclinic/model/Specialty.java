package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specialty", schema = "revitaclinic")
public class Specialty extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToMany(mappedBy = "specialties")
    private Set<Doctor> doctors = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}