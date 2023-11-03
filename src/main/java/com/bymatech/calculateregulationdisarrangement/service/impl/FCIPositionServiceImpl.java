package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FCIPositionServiceImpl implements FCIPositionService {

    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private MarketHttpService marketHttpService;


    public Map<FCISpecieType, List<FCISpeciePosition>> groupPositionBySpecieType(List<FCISpeciePosition> position) {
        return position.stream().collect(Collectors.groupingBy(FCISpeciePosition::getFciSpecieType));
    }

    public Double calculateTotalValuedPosition(List<FCISpeciePosition> position) {
        return calculateTotalValuedPosition(getValuedPositionBySpecieType(groupPositionBySpecieType(position)));
    }

    public Double calculateTotalValuedPosition(Map<FCISpecieType, Double> summarizedPosition) {
        return summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
    }

    public Map<FCISpecieType, Double> getValuedPositionBySpecieType(Map<FCISpecieType, List<FCISpeciePosition>> position) {
        return position.entrySet().stream()
                .map(entry ->
                        Map.entry(entry.getKey(),
                                entry.getValue().stream()
                                        .map(FCISpeciePosition::valuePosition)
                                        .reduce(Double::sum).orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public FCIPosition createFCIPosition(String symbol, FCIPosition fciPosition) throws JsonProcessingException  {
//        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
//        fciPosition.setCreatedOn();
//        fciPosition.setOverview(fciPosition);
////        fciRegulation.getPositions().add(fciPosition);
//        fciRegulationRepository.save(fciRegulation);
//        return fciPosition;
        return null;
    }

    @Override
    public FCIPosition findFCIPositionById(String symbol, Integer fciPositionId) {
//        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
//        return fciRegulation.getPositions().stream().filter(p -> p.getId().equals(fciPositionId)).findFirst()
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format(ExceptionMessage.FCI_POSITION_ENTITY_NOT_FOUND.msg, symbol, fciPositionId)));
        return null;
    }

    public List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition, Boolean refresh) throws Exception {
        marketHttpService.updateCurrentMarketPrices();
        return updateCurrentMarketPriceToPosition(fciPosition);
    }
    @Override
    public List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition) throws Exception {
        List<FCISpeciePosition> fciSpeciePositions = FCIPosition.getSpeciePositions(fciPosition);

        List<FCISpeciePosition> updatedFciSpeciePositions = new ArrayList<>();
        fciSpeciePositions.forEach(fciSpeciePosition -> {
            MarketResponse marketResponse = marketHttpService.getMarketResponses().stream()
                        .filter(response -> response.getMarketSymbol().equals(fciSpeciePosition.getSymbol()))
                        .findFirst().orElseThrow();
        fciSpeciePosition.setCurrentMarketPrice(Double.valueOf(marketResponse.getMarketPrice()));
        updatedFciSpeciePositions.add(fciSpeciePosition);
        });

        return updatedFciSpeciePositions;
    }

    @Override
    public Set<FCIPositionVO> listPositionsByFCIRegulationSymbol(String symbol) {
//        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
////        Set<FCIPosition> positions = fciRegulation.getPositions();
//        Set<FCIPosition> positions = null;
//        return positions.stream().map(p ->
//                FCIPositionVO.builder()
//                        .id(p.getId())
//                        .fciSymbol(fciRegulation.getSymbol())
//                        .timestamp(p.getCreatedOn(DATE_TIME_FORMAT))
//                        .overview(p.getOverview())
//                        .jsonPosition(p.getJsonPosition())
//                        .build()).collect(Collectors.toSet());
        return null;
    }

}
