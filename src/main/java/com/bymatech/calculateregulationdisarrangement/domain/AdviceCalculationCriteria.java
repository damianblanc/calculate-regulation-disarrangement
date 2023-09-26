package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Getter;

@Getter
public enum AdviceCalculationCriteria {

    PRICE_UNIFORMLY_DISTRIBUTION("price_uniformly_distribution", 5),
    ;


    private String label;
    private Integer limit = 5;

    AdviceCalculationCriteria(String label, Integer limit) {
        this.label = label;
        this.limit = limit;
    }
}
