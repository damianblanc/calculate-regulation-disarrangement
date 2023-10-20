package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FCIRegulationPercentageVO {
    List<FCIPercentageDTO> percentages;
}
