package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    public FCIRegulationVO createFCIRegulation(FCIRegulation request) {
       request.setCreatedOn();
       return toValueObject(fciRegulationRepository.save(request));
    }

    @Override
    public String deleteFCIRegulation(String fciSymbol) throws EntityNotFoundException {
        FCIRegulation toDelete = fciRegulationRepository.findBySymbol(fciSymbol)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciSymbol)));
        fciRegulationRepository.delete(toDelete);
        return fciSymbol;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FCIRegulationVO updateFCIRegulation(FCIRegulation fciRegulation) {
        FCIRegulation foundFciRegulation = fciRegulationRepository.findBySymbol(fciRegulation.getSymbol())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulation.getSymbol())));
        foundFciRegulation.setName(fciRegulation.getName());
        foundFciRegulation.setDescription(fciRegulation.getDescription());
//        foundFciRegulation.getComposition().remove(fciRegulation.getComposition())
        foundFciRegulation.setComposition(fciRegulation.getComposition());
//        Set<FCIComposition> fciCompositions = fciRegulation.getComposition().stream().peek(c -> c.setFciRegulation(foundFciRegulation)).collect(Collectors.toSet());
//        foundFciRegulation.getComposition().addAll(fciRegulation.getComposition());

        FCIRegulation savedFciRegulation = fciRegulationRepository.saveAndFlush(foundFciRegulation);

//        fciRegulation.getComposition().forEach(fciComposition -> {
//            fciComposition.setFciRegulation(foundFciRegulation);
//            fciCompositionService.updateComposition(fciComposition);
//        });
//TODO:SEE  Duplicate entry '2' for key 'fci_composition_by_regulation.UK_rwo8fqqo3luww4recmq67kun2' when updating!
        return toValueObject(savedFciRegulation);
    }

    @Override
    public FCIRegulationVO findFCIRegulation(String symbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
//        fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices());
        return toValueObject(fciRegulation);
    }

    @Override
    public FCIRegulation findFCIRegulationEntity(String symbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        return fciRegulation;
    }

    @Override
    public Optional<FCIRegulation> findFCIRegulationOptionalEntity(String symbol) {
        return fciRegulationRepository.findBySymbol(symbol);
    }

    @Override
    public FCIRegulation findOrCreateFCIRegulationEntity(@NotNull FCIRegulationDTO fciRegulationDTO) {
        return fciRegulationRepository.findBySymbol(fciRegulationDTO.getSymbol())
            .orElseGet(() -> fciRegulationRepository.save(FCIRegulation.builder()
                    .name(fciRegulationDTO.getName())
                    .symbol(fciRegulationDTO.getSymbol())
                    .description(fciRegulationDTO.getDescription())
                    .composition(fciRegulationDTO.getComposition())
                    .build()));
    }

    @Override
    public List<FCIRegulationVO> listFCIRegulations() {
        return fciRegulationRepository.findAll().stream().map(this::toValueObject).sorted().toList();
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
    public List<FCICompositionVO> listFCIRegulationPercentages(String fciRegulationSymbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        return toValueObject(fciRegulation.getComposition());
    }

    private List<FCICompositionVO> toValueObject(List<FCIComposition> fciCompositions) {
        AtomicInteger index = new AtomicInteger();
        List<FCISpecieType> specieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();
       return fciCompositions.stream().map(fciComposition -> {
            FCISpecieType fciSpecieType = specieTypes.stream().filter(specieType ->
                    specieType.getFciSpecieTypeId().equals(fciComposition.getFciSpecieTypeId())).findFirst().orElseThrow();
            return new FCICompositionVO(
                    index.getAndIncrement(),
                    fciSpecieType.getName(),
                    String.valueOf(fciComposition.getPercentage()));
        }).toList();
    }

    private FCIRegulationVO toValueObject(FCIRegulation fciRegulation) {
        return FCIRegulationVO.builder()
                .id(fciRegulation.getId())
                .fciSymbol(fciRegulation.getSymbol())
                .name(fciRegulation.getName())
                .description(fciRegulation.getDescription())
                .composition(toValueObject(fciRegulation.getComposition()))
                .build();
    }

}
