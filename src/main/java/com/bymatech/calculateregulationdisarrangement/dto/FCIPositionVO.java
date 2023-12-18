package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@Builder
public class FCIPositionVO implements Comparable<FCIPositionVO> {

    private Integer id;

    private String fciSymbol;

    private String timestamp;

    private String overview;

    private String jsonPosition;

    private String updatedMarketPosition;

    private List<FCIPositionCompositionVO> composition;

    @Override
    public int compareTo(@NotNull FCIPositionVO fciPositionVO) {
        if (this.getId().equals(fciPositionVO.getId())) return 0;
        return this.getId() < fciPositionVO.getId() ? 1 : -1;
    }
}
