package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents FCI Regulation Composition by defining each {@link SpecieTypeGroupEnum} and its percentage
 */
@Entity
@Table(name = "FCIRegulation")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
@Builder
public class FCIRegulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_regulation_id")
    private Integer id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "composition")
//    @OneToMany(mappedBy = "fciRegulationId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "fciRegulationId", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FCIComposition> composition;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FCIPosition> positions;


    private transient Set<FCIComposition> compositionWithIds;

    @Column(name = "advices")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FCIPositionAdvice> FCIPositionAdvices = new ArrayList<>();

//    public Map<String, Double> getFCIRegulationComposition() {
//        return composition.stream()
//            .map(c -> Map.entry(c.getSpecieType(), c.getPercentage()))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
    public Map<FCISpecieType, Double> getCompositionAsSpecieType() {
        return composition.stream().map(c -> Map.entry(c.getFciSpecieType(), c.getPercentage()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
//
//    public static Map<String, Double> getCompositionAsString(Map<SpecieType, Double> composition) {
//        return composition.entrySet().stream().map(entry -> Map.entry(entry.getKey().name(), entry.getValue()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }

}
