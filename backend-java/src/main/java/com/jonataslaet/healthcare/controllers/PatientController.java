package com.jonataslaet.healthcare.controllers;

import com.jonataslaet.healthcare.controllers.dtos.PatientRecordDTO;
import com.jonataslaet.healthcare.services.PatientService;
import org.jspecify.annotations.NonNull;
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

    @PostMapping
    public ResponseEntity<@NonNull PatientRecordDTO> createPatient(
        @RequestBody PatientRecordDTO patientRecordDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRecordDTO));
    }

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