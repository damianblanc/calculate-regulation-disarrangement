package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FCIAdviceController {


    private ImmutableMap<AdviceCalculationCriteria, FCIPositionAdvisorService> services;

    @Autowired
    private CriteriaAdvisorServiceFactory criteriaAdvisorServiceFactory;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @GetMapping("/calculate-bias/fci/{symbol}/position/{id}/advice/criteria/{criteria}")
    public List<OperationAdviceSpecieType> advicePositionByCriteria(
            @PathVariable String symbol, @PathVariable String id, @PathVariable String criteria) throws Exception {
        return criteriaAdvisorServiceFactory
                .select(AdviceCalculationCriteria.valueOf(criteria.toUpperCase()))
                .advice(symbol, id);
    }

//    @PostMapping("/calculate-disarrangement/fci/{symbol}/advice/criteria/{criteria}/specie-type/{specieType}")
//    public List<Map.Entry<SpecieType, Collection<OperationAdviceVO>>> advicePositionByCriteriaSpecieType(
//            @RequestBody FCIPosition fciPosition, @PathVariable String symbol, @PathVariable String criteria,
//            @PathVariable String specieType) throws IllegalArgumentException, JsonProcessingException {
//        return advicePositionByCriteria(fciPosition, symbol, criteria).entrySet().stream().
//                filter(e -> e.getKey() == SpecieType.valueOf(specieType))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }

//    @PostMapping("/calculate-bias/fci/{symbol}/position/{id}/advice/criteria/{criteria}/verbose")
//    public OperationAdviceVerboseVO advicePositionByCriteriaVerbose(
//            @RequestBody FCISpeciePositionDTO fciPosition, @PathVariable String symbol, @PathVariable String id, @PathVariable String criteria) throws Exception {
//        return criteriaAdvisorServiceFactory
//                .select(AdviceCalculationCriteria.valueOf(criteria.toUpperCase()))
//                .advice(symbol, id);
//    }

    @PostMapping("/advice/criteria/parameter-definition")
    public void createAdviceCriteriaParameterDefinition(@RequestBody AdviceCriteriaParameterDefinition criteriaParameterDefinition) {
        fciPositionAdviceService.createCriteriaDefinition(criteriaParameterDefinition);
    }
}
