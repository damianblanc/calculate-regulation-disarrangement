package com.bymatech.calculateregulationdisarrangement.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AdviceCriteriaParameterDefinition {

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
