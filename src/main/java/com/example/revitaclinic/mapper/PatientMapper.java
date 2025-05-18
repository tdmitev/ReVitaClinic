package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.Patient.CreatePatientDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Patient.UpdatePatientDto;
import com.example.revitaclinic.model.Patient;
import com.example.revitaclinic.service.KeycloakService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel="spring",
        injectionStrategy=InjectionStrategy.FIELD,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public abstract class PatientMapper {
    @Autowired KeycloakService keycloakService;

    // Е→DTO
    @Mapping(target="firstName",
            expression="java(keycloakService.getUser(entity.getKeycloakUserId()).getFirstName())")
    @Mapping(target="lastName",
            expression="java(keycloakService.getUser(entity.getKeycloakUserId()).getLastName())")
    @Mapping(target="email",
            expression="java(keycloakService.getUser(entity.getKeycloakUserId()).getEmail())")
    @Mapping(target="roles",
            expression="java(keycloakService.getUserRoles(entity.getKeycloakUserId()))")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target="egn", source="entity.egn")
    @Mapping(target="healthInsuranceLastPayment", source="entity.healthInsuranceLastPayment")
    @Mapping(target="personalDoctorId",
            expression="java(entity.getPersonalDoctor().getKeycloakUserId())")
    public abstract PatientDto toDto(Patient entity);

    // DTO→E (create)
    @Mapping(target="keycloakUserId",          source="dto.keycloakUserId")
    @Mapping(target="egn",                     source="dto.egn")
    @Mapping(target="healthInsuranceLastPayment", source="dto.healthInsuranceLastPayment")
    public abstract Patient toEntity(CreatePatientDto dto);

    // Partial update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="personalDoctor", ignore=true)
    public abstract void updateEntityFromDto(UpdatePatientDto dto,
                                             @MappingTarget Patient entity);
}