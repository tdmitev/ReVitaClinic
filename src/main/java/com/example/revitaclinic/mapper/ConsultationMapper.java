package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.ConsultationMedication.ConsultationMedicationDto;
import com.example.revitaclinic.dto.Medication.MedicationDto;
import com.example.revitaclinic.dto.SickLeave.SickLeaveDto;
import com.example.revitaclinic.model.*;
import org.mapstruct.*;
import java.util.*;

@Mapper(componentModel = "spring",
        uses = {DoctorMapper.class, PatientMapper.class, DiagnosisMapper.class, MedicationMapper.class})
public interface ConsultationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "doctor", target = "doctor")
    @Mapping(source = "patient", target = "patient")
    @Mapping(source = "diagnosis", target = "diagnosis")
    @Mapping(source = "notes", target = "notes")
    @Mapping(target = "medicationEntries", qualifiedByName = "medicationsToDtos")
    @Mapping(source = "sickLeave", target = "sickLeave")
    ConsultationDto toDto(Consultation consultation);

    @Named("medicationsToDtos")
    default List<ConsultationMedicationDto> mapMedications(Set<ConsultationMedication> meds) {
        if (meds == null) return Collections.emptyList();
        List<ConsultationMedicationDto> list = new ArrayList<>();
        for (ConsultationMedication cm : meds) {
            list.add(new ConsultationMedicationDto(
                    medicationToDto(cm.getMedication()),
                    cm.getDosage(),
                    cm.getFrequency(),
                    cm.getDuration()
            ));
        }
        return list;
    }

    @Mapping(source = "id", target = "id")
    MedicationDto medicationToDto(Medication medication);

    default SickLeaveDto map(SickLeave sl) {
        if (sl == null) return null;
        return new SickLeaveDto(sl.getStartDate(), sl.getNumberOfDays());
    }
}