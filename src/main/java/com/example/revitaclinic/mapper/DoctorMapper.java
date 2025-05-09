package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.service.KeycloakService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel="spring",
        injectionStrategy=InjectionStrategy.FIELD,
        unmappedTargetPolicy=ReportingPolicy.IGNORE)
public abstract class DoctorMapper {
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
    public abstract DoctorDto toDto(Doctor entity);

    // DTO→E (create)
    @Mapping(target="keycloakUserId", source="dto.keycloakUserId")
    @Mapping(target="uniqueId",       source="dto.uniqueId")
    @Mapping(target="personal",       source="dto.personal")
    @Mapping(target="specialties",    ignore=true)
    public abstract Doctor toEntity(CreateDoctorDto dto);

    // Partial update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="specialties", ignore=true)
    public abstract void updateEntityFromDto(UpdateDoctorDto dto,
                                             @MappingTarget Doctor entity);
}