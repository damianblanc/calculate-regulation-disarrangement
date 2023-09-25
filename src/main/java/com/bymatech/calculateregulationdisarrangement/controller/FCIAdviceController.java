package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisor;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FCIAdviceController {

    @Autowired
    private FCIPositionAdvisor fciPositionAdvisor;

    private ImmutableMap<AdviceCalculationCriteria, FCIPositionAdvisor> services;

    @Autowired
    private CriteriaAdvisorServiceFactory criteriaAdvisorServiceFactory;

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}")
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByCriteria(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria) throws IllegalArgumentException {
        return criteriaAdvisorServiceFactory
                .select(AdviceCalculationCriteria.valueOf(criteria.toUpperCase()))
                .advice(fciPosition);
    }

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}/specie-type/{specieType}")
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByCriteriaSpecieType(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria,
            @PathVariable String specieType) throws IllegalArgumentException {
        return advicePositionByCriteria(fciPosition, criteria).entrySet().stream().
                filter(e -> e.getKey() == SpecieType.valueOf(specieType))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
