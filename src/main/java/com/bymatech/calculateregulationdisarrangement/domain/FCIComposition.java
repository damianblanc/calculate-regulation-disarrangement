package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FCIComposition")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
/**
 * Respresents a composition element as a list of Composition in a FCIRegulation
 */
public class FCIComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

//    @Column(name = "fci_regulation_id")
//    private int fciRegulationId;

//    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    @JoinColumn(name = "fci_regulation_id", insertable = false, updatable = false)
//    private FCIRegulation fciRegulation;

    @OneToOne
    @MapsId
    @JoinColumn(name = "fciComposition")
    private FCISpecieType fciSpecieType;

    @Column(name = "percentage")
    private Double percentage;
}
