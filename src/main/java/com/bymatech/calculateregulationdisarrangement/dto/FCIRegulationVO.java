package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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

    public List<FCIPositionVO> getPositions() {
        return Objects.isNull(positions) ? List.of() : positions;
    }

    @Override
    public int compareTo(@NotNull FCIRegulationVO fciRegulationVO) {
        if (this.id.equals(fciRegulationVO.getId())) return 0;
        return this.id < fciRegulationVO.getId() ? 1 : -1;
    }
}
