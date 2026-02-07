package com.jonataslaet.healthcare.services;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.factories.PatientFactory;
import com.jonataslaet.healthcare.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void createPatient_shouldSaveAndReturnDTO() {

        PatientRecordDTO input = PatientFactory.createNonSavedPatientRecord();

        Patient savedEntity = PatientFactory.createSavedPatientEntity();
        when(patientRepository.existsByEmail(input.email())).thenReturn(false);
        when(patientRepository.save(any())).thenReturn(savedEntity);

        PatientRecordDTO result = patientService.createPatient(input);

        verify(patientRepository, times(1)).existsByEmail(any());
        verify(patientRepository, times(1)).save(any());

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(savedEntity);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        PatientRecordDTO input = PatientFactory.createNonSavedPatientRecord();

        when(patientRepository.existsByEmail(input.email()))
            .thenReturn(true);

        assertThatThrownBy(() -> patientService.createPatient(input))
            .isInstanceOf(DuplicationException.class)
            .hasMessageContaining("Esse email já existe");

        verify(patientRepository, times(1)).existsByEmail(any());
        verify(patientRepository, never()).save(any());
    }

}