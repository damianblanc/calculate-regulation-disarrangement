package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
@Builder
public class FCIRegulationDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "composition")
//    @OneToMany(mappedBy = "fciRegulationId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "fciRegulationId", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FCIComposition> composition;

    @Column(name = "advices")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FCIPositionAdvice> FCIPositionAdvices;

    public Map<FCISpecieType, Double> getFCIRegulationComposition() {
//        return composition.stream()
//                .map(c -> Map.entry(c.getFciSpecieType(), c.getPercentage()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return null;
    }

    public Map<FCISpecieType, Double> getCompositionAsSpecieType() {
//        return composition.stream().map(c -> Map.entry(c.getFciSpecieType(), c.getPercentage()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return null;
    }

    public static Map<String, Double> getCompositionAsString(Map<FCISpecieType, Double> composition) {
        return composition.entrySet().stream().map(entry -> Map.entry(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}