package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Stats.DoctorCountDto;
import com.example.revitaclinic.mapper.PatientMapper;
import com.example.revitaclinic.model.Patient;
import com.example.revitaclinic.repository.ConsultationRepository;
import com.example.revitaclinic.repository.DiagnosisRepository;
import com.example.revitaclinic.repository.PatientRepository;
import com.example.revitaclinic.repository.SickLeaveRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final ConsultationRepository consultationRepo;
    private final PatientRepository patientRepo;
    private final DiagnosisRepository diagnosisRepo;
    private final SickLeaveRepository sickLeaveRepo;
    private final DoctorService doctorService;
    private final DiagnosisService diagnosisService;
    private final PatientMapper patientMapper;

    public StatisticsServiceImpl(ConsultationRepository consultationRepo,
                                 PatientRepository patientRepo,
                                 DiagnosisRepository diagnosisRepo,
                                 SickLeaveRepository sickLeaveRepo,
                                 DoctorService doctorService,
                                 DiagnosisService diagnosisService,
                                 PatientMapper patientMapper) {
        this.consultationRepo = consultationRepo;
        this.patientRepo = patientRepo;
        this.diagnosisRepo = diagnosisRepo;
        this.sickLeaveRepo = sickLeaveRepo;
        this.doctorService = doctorService;
        this.diagnosisService = diagnosisService;
        this.patientMapper = patientMapper;
    }

    @Override
    public List<PatientDto> patientsByDiagnosis(Integer diagnosisId) {
        List<Patient> patients = consultationRepo.findPatientsByDiagnosis(diagnosisId);
        return patients.stream().map(patientMapper::toDto).toList();
    }

    @Override
    public List<PatientDto> patientsByDoctor(UUID doctorKeycloakId) {
        List<Patient> patients = patientRepo.findByPersonalDoctor_KeycloakUserId(doctorKeycloakId);
        return patients.stream().map(patientMapper::toDto).toList();
    }


    @Override
    public DiagnosisDto mostCommonDiagnosis() {
        Optional<Integer> id = diagnosisRepo.mostCommonDiagnosisId();
        return id.map(diagnosisService::findById).orElse(null);
    }

    @Override
    public List<DoctorCountDto> patientCountPerDoctor() {
        List<Object[]> rows = patientRepo.countPatientsPerDoctor();
        List<DoctorCountDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            UUID docId = (UUID) r[0];
            Long cnt = ((Number) r[1]).longValue();
            result.add(new DoctorCountDto(docId, cnt));
        }
        return result;
    }

    @Override
    public List<DoctorCountDto> consultationCountPerDoctor() {
        List<Object[]> rows = consultationRepo.countConsultationsPerDoctor();
        List<DoctorCountDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            UUID docId = (UUID) r[0];
            Long cnt = ((Number) r[1]).longValue();
            result.add(new DoctorCountDto(docId, cnt));
        }
        return result;
    }

    @Override
    public Integer monthWithMostSickLeaves() {
        return sickLeaveRepo.monthWithMostSickLeaves().orElse(null);
    }

    @Override
    public DoctorDto doctorWithMostSickLeaves() {
        UUID id = consultationRepo.doctorWithMostSickLeaves();
        if (id == null) return null;
        return doctorService.findById(id);
    }
}
