package com.example.revitaclinic.controller;

import com.example.revitaclinic.config.SecurityConfig;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {PatientController.class, SecurityConfig.class})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private JwtRequestPostProcessor patientJwt() {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_PATIENT")))
                .jwt(jwt -> jwt.claim(JwtClaimNames.SUB, UUID.randomUUID().toString()));
    }

    private JwtRequestPostProcessor doctorJwt() {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")))
                .jwt(jwt -> jwt.claim(JwtClaimNames.SUB, UUID.randomUUID().toString()));
    }

    @Test
    void listAllStatusOk() throws Exception {
        given(patientService.findAll()).willReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients").with(doctorJwt()))
                .andExpect(status().isOk());
    }

    @Test
    void listAllStatusForbidden() throws Exception {
        given(patientService.findAll()).willReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients").with(patientJwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMeStatusOk() throws Exception {
        given(patientService.findById(org.mockito.Mockito.any())).willReturn(
                new PatientDto(null,null,null,null,List.of(),null,null,null,null));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/me").with(patientJwt()))
                .andExpect(status().isOk());
    }
}
