package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FCISpecieType")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCISpecieType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_specie_type_id")
        private Integer fciSpecieTypeId;

    private String name;

    private String description;

//    @OneToOne(mappedBy = "fciSpecieType")
//    private FCIComposition fciComposition;

//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "fci_composition_id")
//    private FCIComposition fciComposition;

//    @OneToOne(mappedBy = "fciSpecieType")
//    private FCIComposition fciComposition;

//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "fciSpecieType")
//    private FCIComposition fciComposition;

//    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private FCISpecieTypeGroup fciSpecieTypeGroup;

}
