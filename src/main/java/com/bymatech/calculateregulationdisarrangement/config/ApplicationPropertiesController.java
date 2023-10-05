package com.bymatech.calculateregulationdisarrangement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationPropertiesController {

    @Autowired
    private ApplicationPropertiesConfigBean configBean;

    @GetMapping(value = "/env/schedule.advice.position.fixed.delay.seconds")
    public String advicePositionSchedulerTime() {
        return configBean.getAdvicePositionSchedulerTime();
    }

    @GetMapping(value = "/env/schedule.advice.position.fixed.delay.seconds/{value}")
    public String setAdvicePositionSchedulerTime(@PathVariable String value) {
        configBean.setAdvicePositionSchedulerTime(value);
        return value;
    }
}
