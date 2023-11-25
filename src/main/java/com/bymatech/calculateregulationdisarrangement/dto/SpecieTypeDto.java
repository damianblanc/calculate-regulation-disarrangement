package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecieTypeDto implements Comparable<SpecieTypeDto> {

    private Integer fciSpecieTypeId;

    private String name;

    private String description;

    private Boolean updatable;

    @Override
    public int compareTo(@NotNull SpecieTypeDto specieTypeDto) {
        if (this.getFciSpecieTypeId().equals(specieTypeDto.getFciSpecieTypeId())) return 0;
        return this.getFciSpecieTypeId() < specieTypeDto.getFciSpecieTypeId() ? 1 : -1;
    }
}
