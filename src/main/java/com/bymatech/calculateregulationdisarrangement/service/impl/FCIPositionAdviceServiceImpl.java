package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.AdvisorCriteriaParameter;
import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.AdviceCriteriaParameterDefinition;
import com.bymatech.calculateregulationdisarrangement.dto.PriceUniformlyDistributionCriteriaParameterDTO;
import com.bymatech.calculateregulationdisarrangement.repository.AdviceCriteriaParameterRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCIPositionAdviceRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class FCIPositionAdviceServiceImpl implements FCIPositionAdviceService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private FCIPositionAdviceRepository fciPositionAdviceRepository;

    @Autowired
    private AdviceCriteriaParameterRepository adviceCriteriaParameterRepository;

    @Override
    public FCIPositionAdvice registerAdvice(FCIRegulation fciRegulation, String advice, String owner) {
        return fciPositionAdviceRepository.save(FCIPositionAdvice.builder()
                .owner(owner)
                .timestamp(Timestamp.from(Instant.now()))
                .advice(advice)
                .fciRegulation(fciRegulation).build());
    }

    @Override
    public List<String> getAllAdvices() {
        return fciPositionAdviceRepository.findAll().stream().map(FCIPositionAdvice::getAdvice).toList();
    }

    @Override
    public List<FCIPositionAdvice> listAllAdvices() {
        return fciPositionAdviceRepository.findAll();
    }

    @Override
    public AdvisorCriteriaParameter createCriteriaDefinition(AdviceCriteriaParameterDefinition criteriaParameterDefinition) {
        return adviceCriteriaParameterRepository.save(AdvisorCriteriaParameter.builder()
                .name(criteriaParameterDefinition.getName())
                .description(criteriaParameterDefinition.getDescription())
                .parameters(criteriaParameterDefinition.getParameters())
                .build());
    }

    @Override
    public AdvisorCriteriaParameter findCriteriaParameterDefinitionByName(AdviceCalculationCriteria criteria) {
        return adviceCriteriaParameterRepository.findByName(criteria.name()).orElseThrow(() ->
                new EntityNotFoundException(String.format(ExceptionMessage.ADVISE_CRITERIA_PARAMETER_NOT_DEFINED.msg, criteria.name())));
    }

    @Override
    public PriceUniformlyDistributionCriteriaParameterDTO getParameters(AdviceCalculationCriteria criteria) throws JsonProcessingException {
        AdvisorCriteriaParameter advisorCriteriaParameter =
                findCriteriaParameterDefinitionByName(
                        AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);
        return objectMapper.readValue(advisorCriteriaParameter.getParameters(), PriceUniformlyDistributionCriteriaParameterDTO.class);
    }

}
