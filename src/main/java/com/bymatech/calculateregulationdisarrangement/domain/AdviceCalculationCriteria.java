package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Getter;

@Getter
public enum AdviceCalculationCriteria {

    PRICE_UNIFORMLY_DISTRIBUTION("price_uniformly_distribution"),
    VOLUME_MAX_TRADING("volume_max_trading"),
    ;


    private String label;

    AdviceCalculationCriteria(String label) {
        this.label = label;
    }
}
