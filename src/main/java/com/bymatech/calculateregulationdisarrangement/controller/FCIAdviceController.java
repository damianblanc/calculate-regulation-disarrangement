package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.AdviceCriteriaParameterDefinition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVerboseVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionCriteriaPriceUniformDistributionService;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionCriteriaVolumeMaxTradingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FCIAdviceController {


    private ImmutableMap<AdviceCalculationCriteria, FCIPositionAdvisorService> services;

    @Autowired
    private CriteriaAdvisorServiceFactory criteriaAdvisorServiceFactory;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}")
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByCriteria(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria) throws IllegalArgumentException, JsonProcessingException {
        return criteriaAdvisorServiceFactory
                .select(AdviceCalculationCriteria.valueOf(criteria.toUpperCase()))
                .advice(fciPosition).getOperationAdviceVO();
    }

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}/specie-type/{specieType}")
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByCriteriaSpecieType(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria,
            @PathVariable String specieType) throws IllegalArgumentException, JsonProcessingException {
        return advicePositionByCriteria(fciPosition, criteria).entrySet().stream().
                filter(e -> e.getKey() == SpecieType.valueOf(specieType))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}/verbose")
    public OperationAdviceVerboseVO advicePositionByCriteriaVerbose(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria) throws IllegalArgumentException, JsonProcessingException {
        return criteriaAdvisorServiceFactory
                .select(AdviceCalculationCriteria.valueOf(criteria.toUpperCase()))
                .advice(fciPosition);
    }

    @PostMapping("/advice/criteria/parameter-definition")
    public void createAdviceCriteriaParameterDefinition(@RequestBody AdviceCriteriaParameterDefinition criteriaParameterDefinition) {
        fciPositionAdviceService.createCriteriaDefinition(criteriaParameterDefinition);
    }
}
