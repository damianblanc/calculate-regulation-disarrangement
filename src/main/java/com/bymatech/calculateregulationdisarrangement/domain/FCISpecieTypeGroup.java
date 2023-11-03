package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FCISpecieTypeGroup")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCISpecieTypeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_specie_type_group_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "fci_specie_type_by_group", joinColumns = @JoinColumn(name = "fci_specie_type_group_id"), inverseJoinColumns = @JoinColumn(name = "fci_specie_type_id"))
    private Set<FCISpecieType> fciSpecieTypes = new HashSet<FCISpecieType>();

}
