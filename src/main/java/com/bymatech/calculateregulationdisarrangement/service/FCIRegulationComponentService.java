package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FCIRegulationComponentService {

    List<SpecieType> listSpecieTypes();
}
