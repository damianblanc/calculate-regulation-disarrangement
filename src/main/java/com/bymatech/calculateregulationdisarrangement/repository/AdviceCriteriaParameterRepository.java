package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.AdvisorCriteriaParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdviceCriteriaParameterRepository extends JpaRepository<AdvisorCriteriaParameter, Integer> {

    Optional<AdvisorCriteriaParameter> findByName(String name);
}
