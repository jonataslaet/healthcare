package com.jonataslaet.healthcare.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.jonataslaet.healthcare.controllers.dtos.OpenApiExamples;
import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.controllers.dtos.StandardError;
import com.jonataslaet.healthcare.services.PatientService;
import com.jonataslaet.healthcare.specifications.SpecificationTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @ApiResponse(responseCode = "201", description = "Successful patient registration response"),
        @ApiResponse(
            responseCode = "409",
            description = "Response for attempt to register a duplicated patient",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to register patient with existing email",
                        summary = "Response for email duplication attempt",
                        value = OpenApiExamples.PATIENT_EMAIL_DUPLICATION_EXCEPTION
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Response for invalid patient registration attempt",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to register patient with invalid gender",
                        summary = "Response for invalid gender inclusion attempt",
                        value = OpenApiExamples.PATIENT_GENDER_INVALID_EXCEPTION
                    ),
                    @ExampleObject(
                        name = "Response for attempt to register patient with invalid height or weight",
                        summary = "Response for invalid height or weight inclusion attempt",
                        value = OpenApiExamples.WEIGHT_OR_HEIGHT_INVALID_EXCEPTION
                    )
                }
            )
        )
    })
    @PostMapping
    public ResponseEntity<@NonNull PatientRecordDTO> createPatient(
        @RequestBody @JsonView(PatientRecordDTO.PatientView.CreatePatient.class) PatientRecordDTO patientRecordDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRecordDTO));
    }

    @JsonView(PatientRecordDTO.PatientView.ReadPatient.class)
    @Operation(
        summary = "Get patient by ID",
        description = "Returns a patient by its identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful patient getting response"),
        @ApiResponse(
            responseCode = "404",
            description = "Response for attempt to get a non existing patient",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to gat patient with non existing id",
                        summary = "Response for non existing identifier attempt",
                        value = OpenApiExamples.PATIENT_NOT_FOUND_EXCEPTION
                    )
                }
            )
        )
    })
    @GetMapping("/{patientId}")
    public ResponseEntity<@NonNull PatientRecordDTO> getPatientById(@PathVariable("patientId") Long patientId) {
        PatientRecordDTO foundPatient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(foundPatient);
    }

    @Operation(
        summary = "Update patient by ID",
        description = "Update a patient by its identifier and return the updated patient"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful patient updating response"),
        @ApiResponse(
            responseCode = "404",
            description = "Response for attempt to update a non existing patient",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to update patient with non existing id",
                        summary = "Response for non existing identifier attempt",
                        value = OpenApiExamples.PATIENT_NOT_FOUND_EXCEPTION
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Response for invalid patient updating attempt",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to update patient with invalid gender",
                        summary = "Response for invalid gender updating attempt",
                        value = OpenApiExamples.PATIENT_GENDER_INVALID_EXCEPTION
                    ),
                    @ExampleObject(
                        name = "Response for attempt to update patient with invalid height or weight",
                        summary = "Response for invalid height or weight updating attempt",
                        value = OpenApiExamples.WEIGHT_OR_HEIGHT_INVALID_EXCEPTION
                    )
                }
            )
        )
    })
    @PutMapping("/{patientId}")
    public ResponseEntity<@NonNull PatientRecordDTO> updatePatient(
        @PathVariable("patientId") Long patientId, @RequestBody
        @JsonView(PatientRecordDTO.PatientView.UpdatePatient.class) PatientRecordDTO patientRecordDTO) {

        PatientRecordDTO updatedPatient = patientService.updatePatient(patientId, patientRecordDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @Operation(
        summary = "Delete a patient by ID",
        description = "Delete a patient by its identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Successful patient deleting response"),
        @ApiResponse(
            responseCode = "404",
            description = "Response for attempt to delete a non existing patient",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandardError.class),
                examples = {
                    @ExampleObject(
                        name = "Response for attempt to delete patient with non existing id",
                        summary = "Response for non existing identifier attempt",
                        value = OpenApiExamples.PATIENT_NOT_FOUND_EXCEPTION
                    )
                }
            )
        )
    })
    @DeleteMapping("/{patientId}")
    public ResponseEntity<@NonNull Void> deletePatient(
        @PathVariable("patientId") Long patientId) {

        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}