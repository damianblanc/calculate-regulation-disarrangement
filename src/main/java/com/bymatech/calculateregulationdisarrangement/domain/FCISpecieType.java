package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "specie_type")
@Data
@Builder
public class FCISpecieType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private FCISpecieTypeGroup specieTypeGroup;

}
