package com.jonataslaet.healthcare.repositories;

import com.jonataslaet.healthcare.entities.Patient;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<@NonNull Patient, @NonNull Long>, JpaSpecificationExecutor<@NonNull Patient> {
    boolean existsByEmail(String email);
}
