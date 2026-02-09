package com.jonataslaet.healthcare.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.services.PatientService;
import com.jonataslaet.healthcare.specifications.SpecificationTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
        summary = "List patients",
        description = "Returns a paginated list of patients with optional filters"
    )
    @Parameters({
        @Parameter(name = "fullname", description = "Filter by full name (case insensitive)"),
        @Parameter(name = "email", description = "Filter by email (case insensitive)"),
        @Parameter(name = "birthDate", description = "Filter by birth date (yyyy-MM-dd)"),
        @Parameter(name = "genders", description = "Filter by genders (comma separated values)"),
        @Parameter(name = "minWeight", description = "Minimum weight"),
        @Parameter(name = "maxWeight", description = "Maximum weight"),
        @Parameter(name = "minHeight", description = "Minimum height"),
        @Parameter(name = "maxHeight", description = "Maximum height"),

        @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
        @Parameter(name = "size", description = "Page size", example = "10"),
        @Parameter(name = "sort", description = "Sort criteria (e.g. fullname,asc)")
    })
    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull PatientRecordDTO>> readAllPatients(
        @Parameter(hidden = true) SpecificationTemplate.PatientSpecification patientSpecification,
        @Parameter(hidden = true) Pageable pageable) {
        Page<@NonNull PatientRecordDTO> patientModelPage = patientService.findAll(patientSpecification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(patientModelPage);
    }

    @Operation(
        summary = "Create a patient",
        description = "Creates a new patient in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Patient created successfully"),
        @ApiResponse(responseCode = "409", description = "Email already exists"),
        @ApiResponse(responseCode = "422", description = "Invalid request payload"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<@NonNull PatientRecordDTO> createPatient(
        @RequestBody @JsonView(PatientRecordDTO.PatientView.CreatePatient.class) PatientRecordDTO patientRecordDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRecordDTO));
    }

    @Operation(
        summary = "Get patient by ID",
        description = "Returns a patient by its identifier"
    )
    @GetMapping("/{patientId}")
    public ResponseEntity<@NonNull PatientRecordDTO> getPatientById(@PathVariable("patientId") Long patientId) {
        PatientRecordDTO foundPatient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(foundPatient);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<@NonNull PatientRecordDTO> updatePatient(
        @PathVariable("patientId") Long patientId, @RequestBody PatientRecordDTO patientRecordDTO) {

        PatientRecordDTO updatedPatient = patientService.updatePatient(patientId, patientRecordDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<@NonNull Void> deletePatient(
        @PathVariable("patientId") Long patientId) {

        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}