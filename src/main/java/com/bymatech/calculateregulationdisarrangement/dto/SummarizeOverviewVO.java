package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class SummarizeOverviewVO {

    private Integer fciSpecieTypeGroupQuantity;
    private List<String> fciSpecieTypeGroups;
    private Integer fciSpecieTypesQuantity;
    private Map<String, List<String>> fciSpecieTypes;


    private Integer fciRegulationQuantity; //OK
    private Integer fciPositionQuantity; //OK
    private Integer fciReportsQuantity;
    private Integer fciAdvicesQuantity;


    private Map<String, List<FCICompositionVO>> fciRegulationCompositions; //OK

    private Map<String, Integer> fciRegulationPositionsQuantity; //OK


    private Map<String, Integer> fciPositionsPerMothLastYear; //OK
}
