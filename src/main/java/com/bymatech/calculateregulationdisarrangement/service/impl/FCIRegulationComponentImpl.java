package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FCIRegulationComponentImpl implements FCIRegulationComponentService {
    @Override
    public List<SpecieTypeGroupEnum> listSpecieTypes() {
        return List.of(SpecieTypeGroupEnum.values());
    }
}
