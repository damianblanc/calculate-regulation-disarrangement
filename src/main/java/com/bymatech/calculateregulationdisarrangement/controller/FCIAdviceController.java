package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

import static com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage.ADVISE_CRITERIA_NOT_DEFINED;

@RestController
@RequestMapping("/api/v1")
public class FCIAdviceController {

    @Autowired
    private FCIPositionAdvisorService fciPositionAdvisorService;

    @PostMapping("/calculate-disarrangement/advice/criteria/{criteria}")
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByCriteria(
            @RequestBody FCIPosition fciPosition, @PathVariable String criteria) throws IllegalArgumentException {
        if (AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION_LIMIT_5_ELEMENTS.getLabel().equals(criteria)) {
            return fciPositionAdvisorService.advicePositionByPrice(
                    AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION_LIMIT_5_ELEMENTS, fciPosition);
        }
        throw new IllegalArgumentException(String.format(ExceptionMessage.ADVISE_CRITERIA_NOT_DEFINED.msg, criteria));
    }
}
