package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FCISpeciePositionDTO {
    private List<FCISpeciePosition> position;
}
