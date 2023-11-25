package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPercentageVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationSymbolAndNameVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationValuedVO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCICompositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Comprehends CRUD operations over {@link FCIRegulation}
 */
@Service
@Transactional
public class FCIRegulationServiceImpl implements FCIRegulationCRUDService {

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Autowired
    private FCISpecieTypeGroupService fciSpecieTypeGroupService;

    @Autowired
    private FCICompositionService fciCompositionService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public FCIRegulation createFCIRegulation(FCIRegulation request) {
       return fciRegulationRepository.save(request);
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
        FCIRegulation foundFciRegulation = fciRegulationRepository.findBySymbol(fciRegulation.getSymbol())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulation.getSymbol())));
        foundFciRegulation.setName(fciRegulation.getName());
        foundFciRegulation.setDescription(fciRegulation.getDescription());
        foundFciRegulation.getComposition().clear();
        foundFciRegulation.setComposition(fciRegulation.getComposition());
//        Set<FCIComposition> fciCompositions = fciRegulation.getComposition().stream().peek(c -> c.setFciRegulation(foundFciRegulation)).collect(Collectors.toSet());
        foundFciRegulation.getComposition().addAll(fciRegulation.getComposition());

        FCIRegulation savedFciRegulation = fciRegulationRepository.saveAndFlush(foundFciRegulation);

//        fciRegulation.getComposition().forEach(fciComposition -> {
//            fciComposition.setFciRegulation(foundFciRegulation);
//            fciCompositionService.updateComposition(fciComposition);
//        });

        return savedFciRegulation;
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
    public List<FCIRegulation> listFCIRegulations() {
        return fciRegulationRepository.findAll().stream().sorted().toList();
//                .peek(fciRegulation -> fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices()))
//       return fciRegulations.stream().peek(r -> r.setFciCompositionWithId((r.getFciComposition()))).collect(Collectors.toSet());
    }

    @Override
    public List<String> listFCIRegulationSymbols() {
       return fciRegulationRepository.findAll().stream().sorted().map(FCIRegulation::getSymbol).toList();
    }

    @Override
    public List<FCIRegulationSymbolAndNameVO> listFCIRegulationSymbolsAndNames() {
        AtomicInteger index = new AtomicInteger();
        return fciRegulationRepository.findAll().stream().sorted().map(fciRegulation ->
            new FCIRegulationSymbolAndNameVO(index.getAndIncrement(), fciRegulation.getSymbol(), fciRegulation.getName())).toList();
    }

    @Override
    public List<FCIPercentageVO> listFCIRegulationPercentages(String fciRegulationSymbol) {
        AtomicInteger index = new AtomicInteger();
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        List<FCISpecieType> specieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();
        return fciRegulation.getComposition().stream().map(fciComposition -> {
            FCISpecieType fciSpecieType = specieTypes.stream().filter(specieType ->
                    specieType.getFciSpecieTypeId().equals(fciComposition.getFciSpecieTypeId())).findFirst().orElseThrow();
            return new FCIPercentageVO(
                    index.getAndIncrement(),
                    fciSpecieType.getName(),
                    String.valueOf(fciComposition.getPercentage()));
                }).toList();
    }

}
