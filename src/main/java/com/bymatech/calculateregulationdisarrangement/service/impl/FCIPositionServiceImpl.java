package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionVO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
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

    public Map<SpecieType, List<SpeciePosition>> groupPositionBySpecieType(List<SpeciePosition> position) {
        return position.stream().collect(Collectors.groupingBy(SpeciePosition::getSpecieType));
    }

    public Double calculateTotalValuedPosition(List<SpeciePosition> position) {
        return calculateTotalValuedPosition(getSummarizedPosition(groupPositionBySpecieType(position)));
    }

    public Double calculateTotalValuedPosition(Map<SpecieType, Double> summarizedPosition) {
        return summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
    }

    public Map<SpecieType, Double> getSummarizedPosition(Map<SpecieType, List< SpeciePosition >> position) {
        return position.entrySet().stream()
                .map(entry ->
                        Map.entry(entry.getKey(),
                                entry.getValue().stream()
                                        .map(SpeciePosition::valuePosition)
                                        .reduce(Double::sum).orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public FCIPosition createFCIPosition(String symbol, FCIPosition fciPosition) throws JsonProcessingException  {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        fciPosition.setCreatedOn();
        fciPosition.setOverview(fciPosition);
        fciRegulation.getPositions().add(fciPosition);
        fciRegulationRepository.save(fciRegulation);
        return fciPosition;
    }

    @Override
    public Set<FCIPositionVO> listPositionsByFCIRegulationSymbol(String symbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        Set<FCIPosition> positions = fciRegulation.getPositions();
        return positions.stream().map(p ->
                FCIPositionVO.builder()
                        .id(p.getId())
                        .fciSymbol(fciRegulation.getSymbol())
                        .timestamp(p.getCreatedOn(DATE_TIME_FORMAT))
                        .overview(p.getOverview())
                        .jsonPosition(p.getJsonPosition())
                        .build()).collect(Collectors.toSet());
    }

}
