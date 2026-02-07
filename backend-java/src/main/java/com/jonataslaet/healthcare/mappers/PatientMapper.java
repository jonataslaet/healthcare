package com.jonataslaet.healthcare.mappers;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import org.springframework.beans.BeanUtils;

public class PatientMapper {

    public static Patient toEntity(PatientRecordDTO patientRecordDTO) {
        Patient patient = new Patient();
        BeanUtils.copyProperties(patientRecordDTO, patient);
        return patient;
    }

    public static PatientRecordDTO toDTO(Patient patient) {
        return new PatientRecordDTO(patient.getId(), patient.getFullname(), patient.getEmail(),
            patient.getBirthDate(), patient.getGender(), patient.getWeight(), patient.getHeight()
        );
    }
}
