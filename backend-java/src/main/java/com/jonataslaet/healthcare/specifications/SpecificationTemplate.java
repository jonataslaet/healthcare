package com.jonataslaet.healthcare.specifications;

import com.jonataslaet.healthcare.entities.Patient;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    @And({
        @Spec(path = "fullname", spec = LikeIgnoreCase.class),
        @Spec(path = "email", spec = LikeIgnoreCase.class),
        @Spec(path = "birthDate", spec = Equal.class),
        @Spec(path = "gender", params = "genders", paramSeparator = ',', spec = In.class),
        @Spec(path = "weight", params = "minWeight", spec = GreaterThanOrEqual.class),
        @Spec(path = "weight", params = "maxWeight", spec = LessThanOrEqual.class),
        @Spec(path = "height", params = "minHeight", spec = GreaterThanOrEqual.class),
        @Spec(path = "height", params = "maxHeight", spec = LessThanOrEqual.class)
    })
    public interface PatientSpecification extends Specification<@NonNull Patient> {}

}
