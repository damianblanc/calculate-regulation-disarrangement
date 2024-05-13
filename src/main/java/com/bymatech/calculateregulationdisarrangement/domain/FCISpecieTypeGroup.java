package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "FCISpecieTypeGroup")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents an overall grouping of {@link FCISpecieType} associated with market data retrieving
 */
public class FCISpecieTypeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_specie_type_group_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    private Boolean updatable;

    /** Indicates specie quantity set, one unit for individual
     * or quantity lot, like 100, for grouped representation */
    @Column(name = "lot")
    private Integer lot;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "fci_specie_type_by_group", joinColumns = @JoinColumn(name = "fci_specie_type_group_id"), inverseJoinColumns = @JoinColumn(name = "fci_specie_type_id"))
    private List<FCISpecieType> fciSpecieTypes = new ArrayList<>();
}
