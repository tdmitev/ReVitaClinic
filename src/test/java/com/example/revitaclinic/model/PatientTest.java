package com.example.revitaclinic.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PatientTest {
    @Test
    void setUserUpdatesKeycloakId() {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser();
        user.setKeycloakUserId(id);
        Patient p = new Patient();
        p.setUser(user);
        assertThat(p.getKeycloakUserId()).isEqualTo(id);
    }
}