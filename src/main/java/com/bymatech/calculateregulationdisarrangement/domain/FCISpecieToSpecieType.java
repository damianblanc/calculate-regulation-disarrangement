package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
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
public class FCISpecieToSpecieType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_specie_id")
    private Integer fciSpecieId;

    private String symbol;

    @ManyToOne
    @JoinColumn(name = "fci_specie_type_id")
    private FCISpecieType fciSpecieType;

}
