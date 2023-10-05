package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionCriteriaPriceUniformDistributionService;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionCriteriaVolumeMaxTradingService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Selects Advisor service implementation to execute based on
 * {@link com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria}
 */
@Component
public class CriteriaAdvisorServiceFactory {

    @Autowired
    private FCIPositionCriteriaPriceUniformDistributionService fciPositionCriteriaPriceUniformDistributionService;

    @Autowired
    private FCIPositionCriteriaVolumeMaxTradingService fciPositionCriteriaVolumeMaxTradingService;

    FCIPositionAdvisorService select(AdviceCalculationCriteria criteria) {
        ImmutableMap<AdviceCalculationCriteria, FCIPositionAdvisorService> services = ImmutableMap.<AdviceCalculationCriteria, FCIPositionAdvisorService>builder()
                .put(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION, fciPositionCriteriaPriceUniformDistributionService)
                .put(AdviceCalculationCriteria.VOLUME_MAX_TRADING, fciPositionCriteriaVolumeMaxTradingService)
                .build();
        return services.get(criteria);
    }
}
