package com.bymatech.calculateregulationdisarrangement.util;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainExtractionHelper {

    public static Map<FCISpecieType, Double> getCompositionAsSpecieType(Set<FCIComposition> fciCompositions, List<FCISpecieType> fciSpecieTypes) {
        return fciCompositions.stream().map(c -> Map.entry(
                        fciSpecieTypes.stream().filter(fciSpecieType ->
                                fciSpecieType.getFciSpecieTypeId().equals(c.getFciSpecieTypeId())).findFirst().orElseThrow(), c.getPercentage()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map.Entry<FCISpecieType, Double> findFciSpecieType(Map<FCISpecieType, Double> fciSpecieTypes, String fciSpecieType) {
        return fciSpecieTypes.entrySet().stream().filter(e -> e.getKey().getName().equals(fciSpecieType)).findFirst().orElseThrow();
    }

    public static Map.Entry<FCISpeciePosition, Double> findFciSpeciePosition(Map<FCISpeciePosition, Double> fciSpeciePositions, FCISpeciePosition fciSpeciePosition) {
        return fciSpeciePositions.entrySet().stream().filter(e -> e.getKey().getSymbol().equals(fciSpeciePosition.getSymbol())).findFirst().orElseThrow();
    }
}
