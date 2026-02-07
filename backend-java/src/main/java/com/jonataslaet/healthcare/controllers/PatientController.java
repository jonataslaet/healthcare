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
    public ResponseEntity<@NonNull PatientRecordDTO> createUser(
        @RequestBody PatientRecordDTO patientRecordDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRecordDTO));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<@NonNull PatientRecordDTO> getOperationById(@PathVariable("patientId") Long patientId) {
        PatientRecordDTO foundOperation = patientService.getPatientById(patientId);
        return ResponseEntity.ok(foundOperation);
    }
}