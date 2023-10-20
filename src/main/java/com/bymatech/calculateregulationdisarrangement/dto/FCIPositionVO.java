package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FCIPositionVO {

    private Integer id;

    private String fciSymbol;

    private String timestamp;

    private String overview;

    private String jsonPosition;
}
