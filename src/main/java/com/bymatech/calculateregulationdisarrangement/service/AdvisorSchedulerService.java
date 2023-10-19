package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceSpecieType;
import com.bymatech.calculateregulationdisarrangement.service.impl.FCIPositionCriteriaPriceUniformDistributionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class AdvisorSchedulerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FCIPositionCriteriaPriceUniformDistributionService fciPositionCriteriaPriceUniformDistributionService;

    @Autowired
    private FCIPositionAdviceService FCIPositionAdviceService;

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;

    private static final String BYMA_AUTOMATED_ADVISOR = "Byma Automated Advisor";

//    @Scheduled(fixedRate = 1000)
//    @Scheduled(fixedDelayString = "${schedule.advice.position.fixed.delay.seconds:2}000")
    public List<OperationAdviceSpecieType> advicePosition() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Fixed Delay scheduler:: " + strDate);

        FCIPositionDTO fciPosition = createFCIPosition();
        FCIRegulation fciRegulation = fciRegulationCRUDService.findFCIRegulation("AMR23");

       List<OperationAdviceSpecieType> advice = fciPositionCriteriaPriceUniformDistributionService.advice("AMR23", fciPosition).getOperationAdvicesVO();
        FCIPositionAdviceService.registerAdvice(fciRegulation, objectMapper.writeValueAsString(advice), BYMA_AUTOMATED_ADVISOR);
        List<String> allAdvices = FCIPositionAdviceService.getAllAdvices();
        return advice;
    }

    private FCIPositionDTO createFCIPosition() throws Exception {
        String postJSON = "{\"fciRegulationDTO\":{\"id\":null,\"name\":\"Alpha Mix Rent FCI\",\"symbol\":\"AMR23\",\"composition\":[{\"id\":null,\"specieType\":\"EQUITY\",\"percentage\":30.0},{\"id\":null,\"specieType\":\"BOND\",\"percentage\":50.0},{\"id\":null,\"specieType\":\"CASH\",\"percentage\":20.0}],\"fciregulationComposition\":{\"MARKET_SHARE\":30.0,\"BOND\":50.0,\"CASH\":20.0},\"compositionAsSpecieType\":{\"BOND\":50.0,\"CASH\":20.0,\"EQUITY\":30.0}},\"fciPositionList\":[{\"name\":\"BANCO GALICIA\",\"symbol\":\"GGAL\",\"specieType\":\"EQUITY\",\"price\":3.15,\"quantity\":1500},{\"name\":\"YPF ESTATAL\",\"symbol\":\"YPFD\",\"specieType\":\"EQUITY\",\"price\":8.5,\"quantity\":6000},{\"name\":\"GLOBAL BOND GD41\",\"symbol\":\"GD41\",\"specieType\":\"BOND\",\"price\":0.6,\"quantity\":40000},{\"name\":\"LOCAL BOND T3X4\",\"symbol\":\"T3X4\",\"specieType\":\"BOND\",\"price\":1.4,\"quantity\":30000},{\"name\":\"CASH\",\"symbol\":\"CASH\",\"specieType\":\"CASH\",\"price\":25000.0,\"quantity\":1}]}";
//        String postJSON = "{\"fciRegulationDTO\":{\"id\":null,\"name\":\"Alpha Mix Rent FCI\",\"symbol\":\"AMR23\",\"composition\":[{\"specieType\":\"EQUITY\",\"percentage\":30.0},{\"id\":null,\"specieType\":\"BOND\",\"percentage\":50.0},{\"id\":null,\"specieType\":\"CASH\",\"percentage\":20.0}],\"fciregulationComposition\":{\"EQUITY\":30.0,\"BOND\":50.0,\"CASH\":20.0},\"compositionAsSpecieType\":{\"BOND\":50.0,\"CASH\":20.0,\"EQUITY\":30.0}},\"fciPositionList\":[{\"name\":\"BANCO GALICIA\",\"symbol\":\"GGAL\",\"specieType\":\"EQUITY\",\"price\":3.15,\"quantity\":1500},{\"name\":\"YPF ESTATAL\",\"symbol\":\"YPF\",\"specieType\":\"EQUITY\",\"price\":8.5,\"quantity\":6000},{\"name\":\"GLOBAL BOND GD41\",\"symbol\":\"GD41\",\"specieType\":\"BOND\",\"price\":0.6,\"quantity\":40000},{\"name\":\"LOCAL BOND T3X4\",\"symbol\":\"T3X4\",\"specieType\":\"BOND\",\"price\":1.4,\"quantity\":30000},{\"name\":\"CASH\",\"symbol\":\"CASH\",\"specieType\":\"CASH\",\"price\":25000.0,\"quantity\":1}]}";
        return  objectMapper.readValue(postJSON, FCIPositionDTO.class);
    }
}
