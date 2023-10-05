package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIPositionAdviceRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Comprehends CRUD operations over {@link FCIRegulation}
 */
@Service
public class FCIRegulationCRUDServiceImpl  implements FCIRegulationCRUDService {

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Override
    public FCIRegulation createFCIRegulation(FCIRegulation fciRegulation) {
        return fciRegulationRepository.save(fciRegulation);
    }

    @Override
    public FCIRegulation deleteFCIRegulation(String symbol) throws EntityNotFoundException {
        FCIRegulation toDelete = fciRegulationRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        fciRegulationRepository.delete(toDelete);
        return toDelete;
    }

    @Override
    public FCIRegulation updateFCIRegulation(FCIRegulation fciRegulation) {
        fciRegulationRepository.findBySymbol(fciRegulation.getSymbol())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulation.getSymbol())));
        return fciRegulationRepository.save(fciRegulation);
    }

    @Override
    public FCIRegulation findFCIRegulation(String symbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices());
        return  fciRegulation;
    }

    @Override
    public Optional<FCIRegulation> findFCIRegulationOptional(String symbol) {
        return fciRegulationRepository.findBySymbol(symbol);
    }

    @Override
    public FCIRegulation findOrCreateFCIRegulation(@NotNull FCIRegulationDTO fciRegulationDTO) {
        return fciRegulationRepository.findBySymbol(fciRegulationDTO.getSymbol())
            .orElseGet(() -> fciRegulationRepository.save(FCIRegulation.builder()
                    .name(fciRegulationDTO.getName())
                    .symbol(fciRegulationDTO.getSymbol())
                    .composition(fciRegulationDTO.getComposition())
                    .build()));
    }

    @Override
    public Set<FCIRegulation> listFCIRegulations() {
        return fciRegulationRepository.findAll().stream()
                .sorted(Comparator.comparingInt(FCIRegulation::getId))
                .peek(fciRegulation -> fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices()))
                .collect(Collectors.toSet());
    }
}
