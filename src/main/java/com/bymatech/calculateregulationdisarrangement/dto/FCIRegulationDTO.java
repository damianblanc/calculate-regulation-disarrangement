package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class FCIRegulationDTO {
    private Integer id;
    private String name;
    private String symbol;
    private String description;
    private List<FCIComposition> composition;
    private Set<FCIPositionAdvice> FCIPositionAdvices;
}