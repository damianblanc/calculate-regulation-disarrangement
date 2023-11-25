package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import org.springframework.stereotype.Service;

@Service
public interface FCICompositionService {

    FCIComposition updateComposition(FCIComposition updatingComposition);
}
