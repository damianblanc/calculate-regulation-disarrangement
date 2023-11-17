package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.AdvisorCriteriaParameter;
import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.AdviceCriteriaParameterDefinition;
import com.bymatech.calculateregulationdisarrangement.dto.PriceUniformlyDistributionCriteriaParameterDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FCIPositionAdviceService {

    /**
     * Registers an Advice
     * Note: owner is to be changed to user when implementing security
     * @param advice advice in json format as exposed in http
     * @param owner process executor requester
     * @return Registered advice
     */
    FCIPositionAdvice registerAdvice(FCIRegulation fciRegulation, String advice, String owner);

    List<String> getAllAdvices();

    List<FCIPositionAdvice> listAllAdvices();

    AdvisorCriteriaParameter createCriteriaDefinition(AdviceCriteriaParameterDefinition criteriaParameterDefinition);

    AdvisorCriteriaParameter findCriteriaParameterDefinitionByName(AdviceCalculationCriteria criteria);

    PriceUniformlyDistributionCriteriaParameterDTO getAdviceParameters(AdviceCalculationCriteria criteria) throws JsonProcessingException;

}
