package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FCIRegulationVO implements Comparable<FCIRegulationVO> {

    private Integer id;

    private String fciSymbol;

    private String name;

    private String description;

    private List<FCICompositionVO> composition;

    private List<FCIPositionVO> positions;

    @Override
    public int compareTo(@NotNull FCIRegulationVO fciRegulationVO) {
        if (this.id.equals(fciRegulationVO.getId())) return 0;
        return this.id < fciRegulationVO.getId() ? 1 : -1;
    }
}
