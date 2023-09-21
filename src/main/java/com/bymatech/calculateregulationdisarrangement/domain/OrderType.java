package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Getter;

import javax.lang.model.element.Element;

@Getter
public enum OrderType {

    ASC(1, OperationAdvice.SELL),
    DESC(-1, OperationAdvice.BUY);

    private Integer sign = 1;

    private OperationAdvice operationAdvice;

    OrderType(Integer sign, OperationAdvice operationAdvice) {
        this.sign = sign;
        this.operationAdvice = operationAdvice;
    }

    public static OrderType valueOfSign(double percentage) {
        for (OrderType e : values()) {
            if (e.sign.equals((int) Math.signum(percentage))) {
                return e;
            }
        }
        return null;
    }
}
