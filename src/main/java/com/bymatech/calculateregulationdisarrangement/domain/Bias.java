package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Keeps Disarrangement History
 */
@Entity
@Table(name = "Biases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bias {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

//    private FCISpecieType specieType;
    private Double biasPercentage;

    @ManyToOne
    @JoinColumn(name = "retrieval_id")
    private Retrieval retrieval;

//    @ManyToOne
//    @JoinColumn(name = "fciRegulation_id")
//    private FCIRegulation fciRegulation;
}
