package com.jonataslaet.healthcare.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jonataslaet.healthcare.entities.enums.GenderEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatientRecordDTO (
    Long id,
    String fullname,
    String email,
    LocalDate birthDate,
    GenderEnum gender,
    BigDecimal weight,
    BigDecimal height
) {}
