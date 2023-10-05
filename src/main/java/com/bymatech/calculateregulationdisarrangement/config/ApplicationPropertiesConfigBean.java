package com.bymatech.calculateregulationdisarrangement.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class ApplicationPropertiesConfigBean {
    @Value("${schedule.advice.position.fixed.delay.seconds}")
    private String advicePositionSchedulerTime;

    public ApplicationPropertiesConfigBean(@Value("${schedule.advice.position.fixed.delay.seconds}") String advicePositionSchedulerTime) {
        this.advicePositionSchedulerTime = advicePositionSchedulerTime;
    }
}
