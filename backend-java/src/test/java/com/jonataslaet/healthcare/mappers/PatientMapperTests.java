package com.jonataslaet.healthcare.mappers;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.factories.PatientFactory;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class PatientMapperTests {

    @Test
    void shouldMapNonSavedDTOToNonSavedEntity() {

        PatientRecordDTO nonSavedPatientRecord = PatientFactory.createNonSavedPatientRecord();

        Patient nonSavedPatientEntity = PatientMapper.toEntity(nonSavedPatientRecord);

        assertThat(nonSavedPatientEntity)
            .usingRecursiveComparison()
            .isEqualTo(nonSavedPatientRecord);
    }

    @Test
    void shouldMapNonSavedEntityToNonSavedDTO() {

        Patient nonSavedPatientEntity = PatientFactory.createNonSavedPatientEntity();

        PatientRecordDTO nonSavedPatientRecord = PatientMapper.toDTO(nonSavedPatientEntity);

        assertThat(nonSavedPatientEntity)
            .usingRecursiveComparison()
            .isEqualTo(nonSavedPatientRecord);
    }

    @Test
    void shouldMapNonSavedDTOToSavedEntity() {

        PatientRecordDTO nonSavedPatientDTO = PatientFactory.createNonSavedPatientRecord();

        Patient savedPatientEntity = PatientMapper.toEntity(nonSavedPatientDTO);
        savedPatientEntity.setId(1L);

        assertThat(savedPatientEntity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(nonSavedPatientDTO);
    }

}
