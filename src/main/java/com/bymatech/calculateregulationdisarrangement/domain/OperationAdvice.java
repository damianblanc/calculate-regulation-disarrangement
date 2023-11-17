package com.bymatech.calculateregulationdisarrangement.domain;

public enum OperationAdvice {

    BUY,
    SELL;

    public static OperationAdvice getOperationAdvice(Double percentage) {
        return (percentage < 0) ? BUY : SELL;
    }
}
