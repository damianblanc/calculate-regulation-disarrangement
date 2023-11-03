package com.bymatech.calculateregulationdisarrangement.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "FCIRegulation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
/**
 * Represents FCI Regulation Composition by defining each {@link SpecieTypeGroupEnum} and its percentage
 */
public class FCIRegulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_regulation_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//    @Column(name = "composition")
//    @OneToMany(mappedBy = "fciRegulationId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "fciRegulationId", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy="fciRegulation", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name="fci_regulation_id")
//    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
//    @JoinTable(name = "fci_composition_by_regulation",
//            joinColumns = @JoinColumn(name = "fci_regulation_id"),
//            inverseJoinColumns = @JoinColumn(name = "fci_composition_id"))
    @JoinTable(name = "fci_composition_by_regulation",
            joinColumns =
                    { @JoinColumn(name = "fci_regulation_id", referencedColumnName = "fci_regulation_id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "fci_composition_id", referencedColumnName = "fci_composition_id") })
    private Set<FCIComposition> composition;

//    private Set<FCIComposition> fciCompositionWithId;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<FCIPosition> positions;


//    public Map<FCISpecieType, Double> getCompositionAsSpecieType() {
//        return fciComposition.stream().map(c -> Map.entry(c.getFciSpecieType(), c.getPercentage()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
}
