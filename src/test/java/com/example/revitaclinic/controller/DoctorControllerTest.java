package com.example.revitaclinic.controller;

import com.example.revitaclinic.config.SecurityConfig;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.service.DoctorService;
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
import java.util.Set;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {DoctorController.class, SecurityConfig.class})
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    private JwtRequestPostProcessor doctorJwt() {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")))
                .jwt(jwt -> jwt.claim(JwtClaimNames.SUB, UUID.randomUUID().toString()));
    }

    private JwtRequestPostProcessor patientJwt() {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_PATIENT")))
                .jwt(jwt -> jwt.claim(JwtClaimNames.SUB, UUID.randomUUID().toString()));
    }

    @Test
    void listAllStatusOk() throws Exception {
        given(doctorService.findAll()).willReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors").with(doctorJwt()))
                .andExpect(status().isOk());
    }

    @Test
    void listAllStatusForbidden() throws Exception {
        given(doctorService.findAll()).willReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors").with(patientJwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getOneStatusOk() throws Exception {
        given(doctorService.findById(any())).willReturn(new DoctorDto(null,null,null,null,null,null,null,false,Set.of()));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/" + UUID.randomUUID()).with(doctorJwt()))
                .andExpect(status().isOk());
    }
}
