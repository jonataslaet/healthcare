package com.jonataslaet.healthcare.services;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.exceptions.ResourceNotFoundException;
import com.jonataslaet.healthcare.mappers.PatientMapper;
import com.jonataslaet.healthcare.repositories.PatientRepository;
import org.springframework.beans.BeanUtils;
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
            throw new DuplicationException("Esse email já existe");
        }
        Patient patient = PatientMapper.toEntity(patientRecordDTO);
        return PatientMapper.toDTO(patientRepository.save(patient));
    }

    public PatientRecordDTO getPatientById(Long patientId) {
        Patient patient = getPatientEntity(patientId);
        return PatientMapper.toDTO(patient);
    }

    public Patient getPatientEntity(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() ->
            new ResourceNotFoundException("Paciente não encontrado"));
    }

    @Transactional
    public PatientRecordDTO updatePatient(Long patientId, PatientRecordDTO patientRecordDTO) {
        Patient patientEntity = getPatientEntity(patientId);
        if (!patientEntity.getEmail().equalsIgnoreCase(patientRecordDTO.email())) {
            if (patientRepository.existsByEmail(patientRecordDTO.email())) {
                throw new DuplicationException("Esse email já existe");
            }
        }
        BeanUtils.copyProperties(patientRecordDTO, patientEntity, "id");
        return PatientMapper.toDTO(patientRepository.save(patientEntity));
    }

    @Transactional
    public void deletePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente não encontrado");
        }
        patientRepository.deleteById(patientId);
    }
}
