package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.Doctor.CreateDoctorDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Doctor.UpdateDoctorDto;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.service.KeycloakService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class DoctorMapper {

    @Autowired protected KeycloakService keycloakService;
    @Autowired protected SpecialtyMapper specialtyMapper;

    // --- Entity → DTO ---
    @Mapping(target = "firstName", expression =
            "java(keycloakService.getUser(entity.getKeycloakUserId()).getFirstName())")
    @Mapping(target = "lastName", expression =
            "java(keycloakService.getUser(entity.getKeycloakUserId()).getLastName())")
    @Mapping(target = "email", expression =
            "java(keycloakService.getUser(entity.getKeycloakUserId()).getEmail())")
    @Mapping(target = "roles", expression =
            "java(keycloakService.getUserRoles(entity.getKeycloakUserId()))")
    @Mapping(target = "phone", source = "entity.user.phone")
    @Mapping(target = "uniqueId", source = "entity.uniqueId")
    @Mapping(target = "personal", source = "entity.personal")
    @Mapping(target = "specialties", expression =
            "java(entity.getSpecialties().stream()" +
                    ".map(specialtyMapper::toDto)" +
                    ".collect(java.util.stream.Collectors.toSet()))")
    public abstract DoctorDto toDto(Doctor entity);

    // --- DTO → Entity  ---
    @Mapping(target = "uniqueId", source = "dto.uniqueId")
    @Mapping(target = "personal", source = "dto.personal")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    public abstract Doctor toEntity(CreateDoctorDto dto);

    // --- Partial update ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    public abstract void updateEntityFromDto(UpdateDoctorDto dto, @MappingTarget Doctor entity);
}