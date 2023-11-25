package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.repository.FCICompositionRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCICompositionService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
@Transactional
public class FCICompositionServiceImpl implements FCICompositionService {

    @Autowired
    private FCICompositionRepository fciCompositionRepository;

    public FCIComposition updateComposition(FCIComposition updatingComposition) {
        FCIComposition composition = fciCompositionRepository.findById(updatingComposition.getFciCompositionId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.FCI_COMPOSITION_ENTITY_NOT_FOUND.msg, updatingComposition.getFciCompositionId())));
        composition.setPercentage(updatingComposition.getPercentage());
//        composition.setFciRegulation(updatingComposition.getFciRegulation());
        return fciCompositionRepository.save(composition);
    }

}
