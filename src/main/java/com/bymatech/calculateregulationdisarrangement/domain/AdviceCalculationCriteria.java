package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Getter;

@Getter
public enum AdviceCalculationCriteria {

    PRICE_UNIFORMLY_DISTRIBUTION_LIMIT_5_ELEMENTS("price_uniformly_distribution_limit_5_elements", 5),
    ;


    private String label;
    private Integer limit = 5;

    AdviceCalculationCriteria(String label, Integer limit) {
        this.label = label;
        this.limit = limit;
    }
}
