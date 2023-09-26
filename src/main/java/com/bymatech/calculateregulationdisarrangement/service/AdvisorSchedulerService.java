package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AdvisorSchedulerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FCIPositionAdvisorService fciPositionAdvisor;

    @Autowired
    private FCIPositionAdviceService FCIPositionAdviceService;

    private static final String BYMA_AUTOMATED_ADVISOR = "Byma Automated Advisor";

    @Scheduled(fixedRate = 1000)
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePosition() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Fixed Delay scheduler:: " + strDate);

        Map<SpecieType, Collection<OperationAdviceVO>> advice = fciPositionAdvisor.advice(createFCIPosition());
        FCIPositionAdviceService.registerAdvice(objectMapper.writeValueAsString(advice), BYMA_AUTOMATED_ADVISOR);
        List<String> allAdvices = FCIPositionAdviceService.getAllAdvices();
        return advice;
    }

    private FCIPosition createFCIPosition() throws Exception {
        String postJSON = "{\"fciRegulationDTO\":{\"id\":null,\"name\":\"Alpha Mix Rent FCI\",\"symbol\":\"AMR23\",\"composition\":[{\"specieType\":\"MARKET_SHARE\",\"percentage\":30.0},{\"id\":null,\"specieType\":\"BOND\",\"percentage\":50.0},{\"id\":null,\"specieType\":\"CASH\",\"percentage\":20.0}],\"fciregulationComposition\":{\"MARKET_SHARE\":30.0,\"BOND\":50.0,\"CASH\":20.0},\"compositionAsSpecieType\":{\"BOND\":50.0,\"CASH\":20.0,\"MARKET_SHARE\":30.0}},\"fciPositionList\":[{\"name\":\"BANCO GALICIA\",\"symbol\":\"GGAL\",\"specieType\":\"MARKET_SHARE\",\"price\":3.15,\"quantity\":1500},{\"name\":\"YPF ESTATAL\",\"symbol\":\"YPF\",\"specieType\":\"MARKET_SHARE\",\"price\":8.5,\"quantity\":6000},{\"name\":\"GLOBAL BOND GD41\",\"symbol\":\"GD41\",\"specieType\":\"BOND\",\"price\":0.6,\"quantity\":40000},{\"name\":\"LOCAL BOND T3X4\",\"symbol\":\"T3X4\",\"specieType\":\"BOND\",\"price\":1.4,\"quantity\":30000},{\"name\":\"CASH\",\"symbol\":\"CASH\",\"specieType\":\"CASH\",\"price\":25000.0,\"quantity\":1}]}";
        return  objectMapper.readValue(postJSON, FCIPosition.class);
    }
}
