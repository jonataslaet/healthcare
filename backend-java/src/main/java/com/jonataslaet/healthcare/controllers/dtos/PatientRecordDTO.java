package com.jonataslaet.healthcare.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.jonataslaet.healthcare.entities.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Patient data transfer object")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatientRecordDTO (

    @JsonView({PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "1")
    Long id,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "Jonatas Blendo dos Santos Laet")
    String fullname,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "jonatas@email.com")
    String email,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "1993-03-05")
    LocalDate birthDate,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "MALE")
    GenderEnum gender,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "92.3")
    BigDecimal weight,

    @JsonView({PatientView.CreatePatient.class, PatientView.ReadPatient.class, PatientView.UpdatePatient.class})
    @Schema(example = "1.69")
    BigDecimal height
) {
    public interface PatientView {
        interface CreatePatient {}
        interface ReadPatient {}
        interface UpdatePatient {}
    }
}
