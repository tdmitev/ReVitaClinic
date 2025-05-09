package com.example.revitaclinic.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_user", schema = "revitaclinic")
public class AppUser {

    @Id
    @Column(name = "kc_user_id", nullable = false, length = 50)
    private UUID keycloakUserId;

    @Column(length = 50)
    private String phone;

    public UUID getKeycloakUserId() {
        return keycloakUserId;
    }
    public void setKeycloakUserId(UUID keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}