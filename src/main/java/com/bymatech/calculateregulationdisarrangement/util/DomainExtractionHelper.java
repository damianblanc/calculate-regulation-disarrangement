package com.bymatech.calculateregulationdisarrangement.util;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
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
}
