package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "advisor_criteria_parameter")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvisorCriteriaParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    /**
     * JSON formatted parameter definition
     */
    @Column(name = "parameter_definition")
    private String parameters;
}
