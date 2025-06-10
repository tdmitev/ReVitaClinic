package com.example.revitaclinic.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorTest {
    @Test
    void setUserUpdatesKeycloakId() {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser();
        user.setKeycloakUserId(id);
        Doctor doc = new Doctor();
        doc.setUser(user);
        assertThat(doc.getKeycloakUserId()).isEqualTo(id);
    }
}