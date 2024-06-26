package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCICompositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationService;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
public class FCIRegulationServiceImpl implements FCIRegulationService {

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
        if (fciRegulationRepository.findBySymbol(request.getSymbol()).isPresent()) {
            throw new IllegalArgumentException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_ALREADY_EXISTS.msg,
                    request.getSymbol()));
        }
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
        foundFciRegulation.setComposition(fciRegulation.getComposition());
        FCIRegulation savedFciRegulation = fciRegulationRepository.saveAndFlush(foundFciRegulation);
        return toValueObject(savedFciRegulation);
    }

    @Override
    public FCIRegulationVO findFCIRegulation(String symbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        return toValueObject(fciRegulation);
    }

    @Override
    public FCIRegulation findFCIRegulationEntity(String symbol) throws EntityNotFoundException {
        return fciRegulationRepository.findBySymbol(symbol).orElseThrow(() ->
                new EntityNotFoundException(String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
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
        List<FCIRegulation> fciRegulations = fciRegulationRepository.findAll();
        return fciRegulations.stream().sorted().map(fciRegulation ->
            new FCIRegulationSymbolAndNameVO(index.getAndIncrement(), fciRegulation.getSymbol(), fciRegulation.getName())).toList();
    }

    @Override
    public List<FCICompositionVO> listFCIRegulationPercentages(String fciRegulationSymbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        return toValueObject(fciRegulation.getComposition());
    }

    @Override
    public Map<String, Integer> listRegulationsGroupedByMonthForOneYear() {
        List<FCIRegulationVO> fciRegulations = listFCIRegulations();

        Map<String, List<FCIRegulationVO>> groupedRegulationsPerMonth = fciRegulations.stream()
            .collect(Collectors.groupingBy(regulation -> DateOperationHelper.getMonth(regulation.getCreatedOn())));

        Map<String, Integer> groupedRegulations = new LinkedHashMap<>();
        Month currentMonth = LocalDate.now().getMonth();
        for (int i = 0; i < 12; i++) {
            String m = currentMonth.minus(i).toString();
            String k = m.substring(0, 1).toUpperCase() + m.substring(1).toLowerCase();
            groupedRegulations.put(k, Objects.nonNull(groupedRegulationsPerMonth.get(k)) ? groupedRegulationsPerMonth.get(k).size() : 0);
        }

        return groupedRegulations;
    }

    private List<FCICompositionVO> toValueObject(List<FCIComposition> fciCompositions) {
        AtomicInteger index = new AtomicInteger();
        List<FCISpecieType> specieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();
       return fciCompositions.stream().map(fciComposition -> {
            FCISpecieType fciSpecieType = specieTypes.stream().filter(specieType ->
                    specieType.getFciSpecieTypeId().equals(fciComposition.getFciSpecieTypeId())).findFirst().orElseThrow();
            return new FCICompositionVO(
                    index.getAndIncrement(),
                    fciSpecieType.getFciSpecieTypeId(),
                    fciSpecieType.getName(),
                    String.valueOf(fciComposition.getPercentage()));
        }).toList();
    }

    private FCIPositionVO toValueObject(String fciRegulationSymbol, FCIPosition fciPosition) {
        return FCIPositionVO.builder()
                .id(fciPosition.getId())
                .fciSymbol(fciRegulationSymbol)
                .timestamp(fciPosition.getCreatedOn(DateOperationHelper.DATE_TIME_FORMAT))
                .overview(fciPosition.getOverview())
                .jsonPosition(fciPosition.getJsonPosition())
                .updatedMarketPosition(fciPosition.getUpdatedMarketPosition())
                .composition(FCIPosition.getPositionComposition(fciPosition, true))
                .build();
    }

    private FCIRegulationVO toValueObject(FCIRegulation fciRegulation) {
        return FCIRegulationVO.builder()
                .id(fciRegulation.getId())
                .fciSymbol(fciRegulation.getSymbol())
                .name(fciRegulation.getName())
                .description(fciRegulation.getDescription())
                .createdOn(fciRegulation.getCreatedOn())
                .composition(toValueObject(fciRegulation.getComposition()))
                .positions(fciRegulation.getPositions().stream().map(position -> toValueObject(fciRegulation.getSymbol(), position)).toList())
                .positionQuantity(fciRegulation.getPositions().size())
                .build();
    }

}
