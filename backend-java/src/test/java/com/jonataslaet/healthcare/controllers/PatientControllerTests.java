package com.jonataslaet.healthcare.controllers;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.controllers.dtos.StandardError;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.exceptions.ResourceNotFoundException;
import com.jonataslaet.healthcare.factories.PatientFactory;
import com.jonataslaet.healthcare.services.PatientService;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void shouldCreatePatient() throws Exception {

        PatientRecordDTO dto = PatientFactory.createNonSavedPatientRecord();

        when(patientService.createPatient(any()))
            .thenReturn(dto);

        String responseJson =
            mockMvc.perform(post("/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PatientRecordDTO response =
            objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response)
            .usingRecursiveComparison()
            .isEqualTo(dto);
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        PatientRecordDTO dto = PatientFactory.createNonSavedPatientRecord();

        when(patientService.createPatient(any()))
            .thenThrow(new DuplicationException("Esse email já existe"));

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict())
            .andExpect(content().string(containsString("Esse email já existe")));
    }

    @Test
    void shouldReturnPatientById() throws Exception {

        PatientRecordDTO dto = PatientFactory.createSavedPatientRecord();

        when(patientService.getPatientById(PatientFactory.existingPatientId)).thenReturn(dto);

        String responseJson =
            mockMvc.perform(get("/patients/{id}", PatientFactory.existingPatientId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PatientRecordDTO response =
            objectMapper.readValue(responseJson, PatientRecordDTO.class);

        assertThat(response)
            .usingRecursiveComparison()
            .isEqualTo(dto);
    }

    @Test
    void shouldReturn404WhenPatientNotFound() throws Exception {

        when(patientService.getPatientById(PatientFactory.nonExistingPatientId))
            .thenThrow(new ResourceNotFoundException("Paciente não encontrado"));

        String responseJson =
            mockMvc.perform(get("/patients/{id}", PatientFactory.nonExistingPatientId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        StandardError error = objectMapper.readValue(responseJson, StandardError.class);

        assertThat(error.getStatus()).isEqualTo(404);
        assertThat(error.getError()).isEqualTo("Recurso não encontrado");
        assertThat(error.getMessage()).contains("Paciente não encontrado");
        assertThat(error.getPath()).isEqualTo("/patients/"+PatientFactory.nonExistingPatientId);
        assertThat(error.getTimestamp()).isNotNull();
    }

}
