package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FCIPositionServiceImpl implements FCIPositionService {

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
}
