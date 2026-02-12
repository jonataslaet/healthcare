package com.jonataslaet.healthcare.controllers.dtos;

public final class OpenApiExamples {

    public static final String PATIENT_EMAIL_DUPLICATION_EXCEPTION = """
        {
          "error": "Erro de requisição",
          "message": "Esse email já existe",
          "path": "/patients",
          "status": 409,
          "timestamp": "2026-02-11T20:26:13.356716700Z"
        }
        """;

    public static final String PATIENT_GENDER_INVALID_EXCEPTION = """
        {
          "error": "Erro de requisição",
          "message": "JSON parse error: Cannot construct instance of `com.jonataslaet.healthcare.entities.enums.GenderEnum`, problem: O valor MALEs é inválido",
          "path": "/patients",
          "status": 400,
          "timestamp": "2026-02-12T12:37:37.919491600Z"
        }
        """;

    public static final String PATIENT_NOT_FOUND_EXCEPTION = """
        {
            "error": "Recurso não encontrado",
            "message": "Paciente não encontrado",
            "path": "/patients/999",
            "status": 404,
            "timestamp": "2026-02-12T21:38:46.569390300Z"
        }
        """;

    public static final String WEIGHT_OR_HEIGHT_INVALID_EXCEPTION = """
        {
            "error": "Erro de requisição",
            "message": "Field 'weight' expects type BigDecimal but received value 'abc'",
            "path": "/patients",
            "status": 400,
            "timestamp": "2026-02-12T22:22:04.389140600Z"
        }
        """;
}

