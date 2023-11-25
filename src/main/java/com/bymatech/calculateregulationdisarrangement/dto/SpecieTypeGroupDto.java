package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecieTypeGroupDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean updatable;

    private List<SpecieTypeDto> fciSpecieTypes = new ArrayList<>();
}
