package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "FCISpecieType")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCISpecieType implements Comparable<FCISpecieType> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_specie_type_id")
    private Integer fciSpecieTypeId;

    private String name;

    private String description;

    /**
     * Indicates whether this specie type accepts their bound species to have its prices
     * updated with current market price
     * Refer to Cash specie type that is not to be updated
     * When not updatable group must have only one specie type
     */
    private Boolean updatable;

    @Column(name = "specie_quantity")
    private Integer specieQuantity = 0;

    @Override
    public int compareTo(@NotNull FCISpecieType fciSpecieType) {
            if (this.getFciSpecieTypeId().equals(fciSpecieType.getFciSpecieTypeId())) return 0;
            return this.getFciSpecieTypeId() < fciSpecieType.getFciSpecieTypeId() ? 1 : -1;
    }

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
