package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedCompositionId implements Serializable {

   Integer fciRegulationId;
   Integer fciCompositionId;
}
