package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FCIRegulationComponentImpl implements FCIRegulationComponentService {
    @Override
    public List<SpecieType> listSpecieTypes() {
        return List.of(SpecieType.values());
    }
}
