package com.jonataslaet.healthcare.factories;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.entities.enums.GenderEnum;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public final class PatientFactory {

    public static Patient createSavedPatientEntity() {
        Patient patient = new Patient();
        BeanUtils.copyProperties(createSavedPatientRecord(), patient);
        return patient;
    }

    public static PatientRecordDTO createSavedPatientRecord() {
        return new PatientRecordDTO(1L, "Jonatas Blendo dos Santos Laet",
            "jonataslaetprogramador@gmail.com", LocalDate.of(1993, Month.MARCH, 5),
            GenderEnum.MALE, new BigDecimal("92.3"), new BigDecimal("1.69"));
    }

    public static Patient createNonSavedPatientEntity() {
        Patient patient = new Patient();
        BeanUtils.copyProperties(createSavedPatientRecord(), patient);
        patient.setId(null);
        return patient;
    }

    public static PatientRecordDTO createNonSavedPatientRecord() {
        return new PatientRecordDTO(null, "Jonatas Blendo dos Santos Laet",
            "jonataslaetprogramador@gmail.com", LocalDate.of(1993, Month.MARCH, 5),
            GenderEnum.MALE, new BigDecimal("92.3"), new BigDecimal("1.69"));
    }
}
