package com.jonataslaet.healthcare.services;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.entities.Patient;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.exceptions.ResourceNotFoundException;
import com.jonataslaet.healthcare.factories.PatientFactory;
import com.jonataslaet.healthcare.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

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

    private Long existingPatientId;
    private Long nonExistingPatientId;

    @BeforeEach
    void setUp() {
        existingPatientId = PatientFactory.existingPatientId;
        nonExistingPatientId = PatientFactory.nonExistingPatientId;
    }

    @Test
    void createPatient_shouldSaveAndReturnDTO() {

        PatientRecordDTO input = PatientFactory.createNonSavedPatientRecord();

        Patient savedEntity = PatientFactory.createSavedPatientEntity();
        when(patientRepository.existsByEmail(input.email())).thenReturn(false);
        when(patientRepository.save(any())).thenReturn(savedEntity);

        PatientRecordDTO result = patientService.createPatient(input);

        verify(patientRepository, times(1)).existsByEmail(any());
        verify(patientRepository, times(1)).save(any());

        assertThat(result).usingRecursiveComparison().isEqualTo(savedEntity);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        PatientRecordDTO input = PatientFactory.createNonSavedPatientRecord();

        when(patientRepository.existsByEmail(input.email())).thenReturn(true);

        assertThatThrownBy(() -> patientService.createPatient(input))
            .isInstanceOf(DuplicationException.class).hasMessageContaining("Esse email já existe");

        verify(patientRepository, times(1)).existsByEmail(any());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void getPatientById_shouldReturnDTOWhenFound() {

        Patient saved = PatientFactory.createSavedPatientEntity();

        when(patientRepository.findById(existingPatientId))
            .thenReturn(java.util.Optional.of(saved));

        PatientRecordDTO result = patientService.getPatientById(existingPatientId);

        verify(patientRepository).findById(existingPatientId);

        assertThat(result).usingRecursiveComparison().isEqualTo(saved);
    }

    @Test
    void getPatientById_shouldThrowWhenNotFound() {

        when(patientRepository.findById(nonExistingPatientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.getPatientById(nonExistingPatientId))
            .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("Paciente não encontrado");

        verify(patientRepository).findById(nonExistingPatientId);
    }

    @Test
    void updatePatient_shouldReturnDTOWhenFoundWithDifferentEmails() {

        Patient existing = PatientFactory.createSavedPatientEntity();
        existing.setEmail("changedemail@gmail.com");
        PatientRecordDTO input = PatientFactory.createSavedPatientRecord();

        when(patientRepository.findById(existingPatientId)).thenReturn(Optional.of(existing));
        when(patientRepository.existsByEmail(input.email())).thenReturn(false);
        when(patientRepository.save(any())).thenReturn(existing);

        PatientRecordDTO result = patientService.updatePatient(existingPatientId, input);

        verify(patientRepository).existsByEmail(input.email());
        verify(patientRepository).save(existing);

        assertThat(result).usingRecursiveComparison().isEqualTo(existing);
    }


    @Test
    void updatePatient_shouldReturnDTOWhenFoundWithSameEmails() {

        Patient existing = PatientFactory.createSavedPatientEntity();
        PatientRecordDTO input = PatientFactory.createSavedPatientRecord();

        when(patientRepository.findById(existingPatientId)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any())).thenReturn(existing);

        PatientRecordDTO result = patientService.updatePatient(existingPatientId, input);

        verify(patientRepository, never()).existsByEmail(any());
        verify(patientRepository).save(existing);

        assertThat(result).usingRecursiveComparison().isEqualTo(existing);
    }


    @Test
    void updatePatient_shouldThrowWhenNotFound() {

        PatientRecordDTO input = PatientFactory.createSavedPatientRecord();

        when(patientRepository.findById(nonExistingPatientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.updatePatient(nonExistingPatientId, input))
            .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("Paciente não encontrado");

        verify(patientRepository).findById(nonExistingPatientId);
        verify(patientRepository, never()).save(any());
    }

}