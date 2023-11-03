package com.bymatech.calculateregulationdisarrangement.util;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainExtractionHelper {

    public static Map<FCISpecieType, Double> getCompositionAsSpecieType(Set<FCIComposition> fciCompositions) {
//        return fciCompositions.stream().map(c -> Map.entry(c.getFciSpecieType(), c.getPercentage()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return null;
    }
}
