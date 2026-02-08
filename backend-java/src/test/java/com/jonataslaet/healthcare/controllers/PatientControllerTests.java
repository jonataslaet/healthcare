package com.jonataslaet.healthcare.controllers;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.controllers.dtos.StandardError;
import com.jonataslaet.healthcare.entities.enums.GenderEnum;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.exceptions.ResourceNotFoundException;
import com.jonataslaet.healthcare.factories.PatientFactory;
import com.jonataslaet.healthcare.services.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PatientController.class)
class PatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    private Long existingPatientId;
    private Long nonExistingPatientId;

    @BeforeEach
    void setUp() {
        existingPatientId = PatientFactory.existingPatientId;
        nonExistingPatientId = PatientFactory.nonExistingPatientId;
    }

    @Test
    void shouldCreatePatient() throws Exception {

        PatientRecordDTO dto = PatientFactory.createNonSavedPatientRecord();

        when(patientService.createPatient(any())).thenReturn(dto);

        String responseJson = mockMvc.perform(post("/patients")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        PatientRecordDTO response = objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        PatientRecordDTO dto = PatientFactory.createNonSavedPatientRecord();

        when(patientService.createPatient(any())).thenThrow(new DuplicationException("Esse email já existe"));

        String responseJson = mockMvc.perform(post("/patients")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict()).andReturn().getResponse().getContentAsString();

        StandardError error = objectMapper.readValue(responseJson, StandardError.class);

        assertThat(error.getStatus()).isEqualTo(409);
        assertThat(error.getError()).isEqualTo("Erro de requisição");
        assertThat(error.getMessage()).contains("Esse email já existe");
        assertThat(error.getPath()).isEqualTo("/patients");
        assertThat(error.getTimestamp()).isNotNull();
    }

    @Test
    void shouldReturnPatientById() throws Exception {

        PatientRecordDTO dto = PatientFactory.createSavedPatientRecord();

        when(patientService.getPatientById(existingPatientId)).thenReturn(dto);

        String responseJson = mockMvc.perform(get("/patients/{id}", existingPatientId))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PatientRecordDTO response = objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void shouldReturn404WhenPatientNotFound() throws Exception {

        when(patientService.getPatientById(nonExistingPatientId))
            .thenThrow(new ResourceNotFoundException("Paciente não encontrado"));

        String responseJson = mockMvc.perform(get("/patients/{id}", nonExistingPatientId))
            .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        StandardError error = objectMapper.readValue(responseJson, StandardError.class);

        assertThat(error.getStatus()).isEqualTo(404);
        assertThat(error.getError()).isEqualTo("Recurso não encontrado");
        assertThat(error.getMessage()).contains("Paciente não encontrado");
        assertThat(error.getPath()).isEqualTo("/patients/"+nonExistingPatientId);
        assertThat(error.getTimestamp()).isNotNull();
    }

    @Test
    void shouldReturn400WhenGenderIsInvalid() throws Exception {

        String jsonPatient = PatientFactory.createJsonNonSavedPatientRecordWithInvalidGender();
        String responseJson = mockMvc.perform(post("/patients").contentType(MediaType.APPLICATION_JSON)
            .content(jsonPatient)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        StandardError error = objectMapper.readValue(responseJson, StandardError.class);

        assertThat(error.getStatus()).isEqualTo(400);
        assertThat(error.getError()).isEqualTo("Erro de requisição");
        assertThat(error.getMessage()).contains("O valor INVALID é inválido");
        assertThat(error.getPath()).isEqualTo("/patients");
        assertThat(error.getTimestamp()).isNotNull();
    }

    @Test
    void updatePatient_shouldReturnDTOWhenFoundWithDifferentEmails() throws Exception {

        PatientRecordDTO input = PatientFactory.createNonSavedPatientRecord();
        PatientRecordDTO updated = PatientFactory.createSavedPatientRecord();

        when(patientService.updatePatient(existingPatientId, input)).thenReturn(updated);

        String responseJson = mockMvc.perform(put("/patients/{id}", existingPatientId)
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PatientRecordDTO response = objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(updated);
    }

    @Test
    void updatePatient_shouldReturnDTOWhenFoundWithSameEmails() throws Exception {

        PatientRecordDTO input = PatientFactory.createSavedPatientRecord();

        when(patientService.updatePatient(existingPatientId, input)).thenReturn(input);

        String responseJson = mockMvc.perform(put("/patients/{id}", existingPatientId)
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PatientRecordDTO response = objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(input);
    }

    @Test
    void updatePatient_shouldThrowWhenNotFound() throws Exception {

        PatientRecordDTO input = PatientFactory.createSavedPatientRecord();

        when(patientService.updatePatient(eq(nonExistingPatientId), any()))
            .thenThrow(new ResourceNotFoundException("Paciente não encontrado"));

        mockMvc.perform(put("/patients/{id}", nonExistingPatientId).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))).andExpect(status().isNotFound())
            .andExpect(content().string(containsString("Paciente não encontrado")));
    }

    @Test
    void shouldDeletePatientById() throws Exception {

        PatientRecordDTO dto = PatientFactory.createSavedPatientRecord();

        when(patientService.getPatientById(existingPatientId)).thenReturn(dto);

        String responseJson = mockMvc.perform(delete("/patients/{id}", existingPatientId))
            .andExpect(status().isNoContent()).andReturn().getResponse().getContentAsString();

        assertThat(responseJson).isEqualTo("");
    }

    @ParameterizedTest
    @EnumSource(GenderEnum.class)
    void shouldAcceptAllGenderEnumValues(GenderEnum gender) {
        assertThat(GenderEnum.from(gender.name())).isEqualTo(gender);
    }
}
