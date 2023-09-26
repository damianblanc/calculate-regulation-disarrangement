package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionAdvisorPriceUniformlyDistributionService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Selects Advisor service implementation to execute based on
 * {@link com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria}
 */
@Component
public class CriteriaAdvisorServiceFactory {

    private ImmutableMap<AdviceCalculationCriteria, FCIPositionAdvisorService> services;

    @Autowired
    private FCIPositionAdvisorPriceUniformlyDistributionService fciPositionAdvisorPriceUniformlyDistributionService;

    public FCIPositionAdvisorService select(AdviceCalculationCriteria criteria) {
        services = ImmutableMap.<AdviceCalculationCriteria, FCIPositionAdvisorService>builder()
                .put(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION_LIMIT_5_ELEMENTS,
                        fciPositionAdvisorPriceUniformlyDistributionService)
                .build();
        return services.get(criteria);
    }
}
