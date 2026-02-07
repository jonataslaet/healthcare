package com.jonataslaet.healthcare.services;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.mappers.PatientMapper;
import com.jonataslaet.healthcare.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional
    public PatientRecordDTO createPatient(PatientRecordDTO patientRecordDTO) {
        if (patientRepository.existsByEmail(patientRecordDTO.email())) {
            throw new IllegalArgumentException("Esse email já existe");
        }
        Patient patient = PatientMapper.toEntity(patientRecordDTO);
        return PatientMapper.toDTO(patientRepository.save(patient));
    }

}
