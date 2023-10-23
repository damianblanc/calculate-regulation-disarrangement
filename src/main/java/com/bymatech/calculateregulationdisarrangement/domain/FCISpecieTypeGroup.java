package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specie_type_group")
@Data
@Builder
public class FCISpecieTypeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @OneToMany
    @JoinTable(name = "specie_type_by_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "specie_type_id"))
    private Set<FCISpecieType> fciSpecieTypes = new HashSet<FCISpecieType>();

}
