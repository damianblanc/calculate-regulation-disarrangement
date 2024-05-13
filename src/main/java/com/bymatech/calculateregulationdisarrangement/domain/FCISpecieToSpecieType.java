package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FCISpecieToSpecieType")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents an association between a {@link FCISpeciePosition} and {@link FCISpecieType}
 * A {@link FCISpecieTypeGroup} contains many {@link FCISpecieType}
 */
public class FCISpecieToSpecieType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "specie_symbol")
    private String specieSymbol;

    @ManyToOne
    @JoinColumn(name = "fci_specie_type_id")
    private FCISpecieType fciSpecieType;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "specie_to_specie_type_position_Id", referencedColumnName = "id")
    private List<FCISpecieToSpecieTypePosition> positions;
}
