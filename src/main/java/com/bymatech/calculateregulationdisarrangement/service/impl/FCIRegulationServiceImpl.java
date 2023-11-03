package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Comprehends CRUD operations over {@link FCIRegulation}
 */
@Service
public class FCIRegulationServiceImpl implements FCIRegulationCRUDService {

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public FCIRegulation createFCIRegulation(FCIRegulation request) {
       return  fciRegulationRepository.save(request);
//        FCIRegulation fciRegulation = FCIRegulation.builder().symbol(request.getSymbol()).name(request.getName()).description(request.getDescription()).build();
//        FCIRegulation savedFciRegulation = fciRegulationRepository.save(fciRegulation);
//        Set<FCIComposition> fciCompositions = new HashSet<>();
//        request.get().forEach(c ->
//                fciCompositions.add(FCIComposition.builder().fciRegulation(fciRegulation).fciSpecieType(c.getFciSpecieType()).percentage(c.getPercentage()).build()));
//        savedFciRegulation.setComposition(fciCompositions);
//        return fciRegulationRepository.save(savedFciRegulation);
//        FCIRegulation persistedFCIRegulation = fciRegulationRepository.save(fciRegulation);
//request.setFciComposition(fciCompositions);
//        persistedFCIRegulation.getFciComposition().forEach(c -> c.setFciRegulationId(persistedFCIRegulation.getFciRegulationId()));
//        return fciRegulationRepository.save(persistedFCIRegulation);
//        return fciRegulationRepository.save(request);
//        return persistedFCIRegulation;
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
//        fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices());
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
//                    .fciCompositionWithId(fciRegulationDTO.getComposition())
                    .build()));
    }

    @Override
    public Set<FCIRegulation> listFCIRegulations() {
        Set<FCIRegulation> fciRegulations = fciRegulationRepository.findAll().stream()
//                .peek(fciRegulation -> fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices()))
                .collect(Collectors.toSet());
//       return fciRegulations.stream().peek(r -> r.setFciCompositionWithId((r.getFciComposition()))).collect(Collectors.toSet());
       return fciRegulations;
    }
}
