package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecieTypeGroupDto implements Comparable<SpecieTypeGroupDto> {
    private Integer id;

    private String name;

    private String description;

    private Boolean updatable;

    private List<SpecieTypeDto> fciSpecieTypes = new ArrayList<>();

    @Override
    public int compareTo(@NotNull SpecieTypeGroupDto specieTypeGroupDto) {
        if (id.equals(specieTypeGroupDto.getId())) return 0;
        return id < specieTypeGroupDto.getId() ? -1 : 1;
    }
}
