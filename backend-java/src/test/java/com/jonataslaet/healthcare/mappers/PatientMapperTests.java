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
    void shouldMapDTOToEntity() {

        PatientRecordDTO dto = PatientFactory.createSavedPatientRecord();

        Patient entity = PatientMapper.toEntity(dto);

        assertThat(entity)
            .usingRecursiveComparison()
            .isEqualTo(dto);
    }

    @Test
    void shouldMapEntityToDTO() {

        Patient entity = PatientFactory.createSavedPatientEntity();

        PatientRecordDTO dto = PatientMapper.toDTO(entity);

        assertThat(entity)
            .usingRecursiveComparison()
            .isEqualTo(dto);
    }
}
