package com.bymatech.calculateregulationdisarrangement.domain;

import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
/**
 * Represents FCI Regulation Composition by defining each {@link SpecieType} and its percentage
 */
public class FCIRegulation {

    private String name;
    private Map<SpecieType, Double> composition;

}
