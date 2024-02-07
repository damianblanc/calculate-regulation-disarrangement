package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.exception.PositionValidationException;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.DoubleBuffer;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FCIPositionServiceImpl implements FCIPositionService {

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private MarketHttpService marketHttpService;

    @Autowired
    private FCISpecieTypeGroupService fciSpecieTypeGroupService;


    public Map<FCISpecieType, List<FCISpeciePosition>> groupPositionBySpecieType(List<FCISpeciePosition> position, List<FCISpecieType> fciSpecieTypes) {
        return position.stream().collect(Collectors.groupingBy(fciSpeciePosition ->
                fciSpecieTypes.stream()
                        .filter(fciSpecieType -> fciSpecieType.getName().equals(fciSpeciePosition.getFciSpecieType()))
                        .findFirst().orElseThrow()));
    }

    public Double calculateTotalValuedPosition(List<FCISpeciePosition> position, List<FCISpecieType> fciSpecieTypes) {
        return calculateTotalValuedPosition(getValuedPositionBySpecieType(groupPositionBySpecieType(position, fciSpecieTypes)));
    }

    public Double calculateTotalValuedPosition(Map<FCISpecieType, Double> summarizedPosition) {
        return summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
    }

    public Map<FCISpecieType, Double> getValuedPositionBySpecieType(Map<FCISpecieType, List<FCISpeciePosition>> position) {
        return position.entrySet().stream()
                .map(entry ->
                        Map.entry(entry.getKey(),
                                entry.getValue().stream()
                                        .map(FCISpeciePosition::valuePosition)
                                        .reduce(Double::sum).orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * All species included in {@link FCIPosition} must be associated to a {@link FCISpecieType }, then to a {@link FCISpecieTypeGroup}
     */
    @Override
    public FCIPositionVO createFCIPosition(String fciRegulationSymbol, FCIPosition fciPosition) throws Exception {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));

        attachSpecieTypeAndGroup(fciPosition);

        validatePosition(fciPosition, fciRegulation);

        adaptCashGroupInPosition(fciPosition);

        fciPosition.setCreatedOn();
        List<FCISpeciePosition> currentMarketPriceToPosition = updateCurrentMarketPriceToPosition(fciPosition, true);
        fciPosition.updateMarketPosition(currentMarketPriceToPosition);
        fciPosition.setOverview(currentMarketPriceToPosition);

        fciRegulation.getPositions().add(fciPosition);
        fciRegulationRepository.save(fciRegulation);
        return createFCIPositionVO(fciRegulationSymbol, fciPosition);
    }

    /**
     * Since simplification to receive cash total in quantity column it must be changed its setting, being quantity = 1
     * and currentMarketPrice = quantity, the amount of cash in position
     */
    private static void adaptCashGroupInPosition(FCIPosition fciPosition) throws JsonProcessingException {
        List<FCISpeciePosition> speciePositions = FCIPosition.getSpeciePositions(fciPosition, false);
        Optional<FCISpeciePosition> optionalCash = speciePositions.stream().filter(specieInPosition ->
                specieInPosition.getSymbol().equals(SpecieTypeGroupEnum.Cash.name())).findFirst();
        if (optionalCash.isPresent()) {
            FCISpeciePosition cash = optionalCash.get();
            cash.setCurrentMarketPrice((double) cash.getQuantity());
            cash.setQuantity(1);
        }
        fciPosition.updateOriginalPosition(speciePositions);
    }

    private void attachSpecieTypeAndGroup(FCIPosition fciPosition) throws JsonProcessingException {
        List<SpecieToSpecieType> specieToSpecieTypes = fciSpecieTypeGroupService.listAllSpecieToSpecieTypeAssociations();
        List<FCISpeciePosition> fciSpeciePositions = FCIPosition.getSpeciePositions(fciPosition, false).stream()
                .peek(fciSpeciePosition -> {
                            SpecieToSpecieType specieToSpecieType = specieToSpecieTypes.stream().filter(association -> fciSpeciePosition.getSymbol().equals(association.getSpecieSymbol())).findFirst()
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            String.format(ExceptionMessage.SPECIE_TO_SPECIE_TYPE_DOES_NOT_EXIST.msg, fciSpeciePosition.getSymbol())));
                            fciSpeciePosition.setFciSpecieType(specieToSpecieType.getSpecieTypeName());
                            fciSpeciePosition.setFciSpecieGroup(specieToSpecieType.getSpecieTypeGroupName());
                        }).toList();
        fciPosition.setTransientJsonPosition(fciSpeciePositions);
    }

    @Override
    public Integer deleteFCIPosition(String fciRegulationSymbol, Integer fciPositionId) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        fciRegulation.getPositions().removeIf(fciPosition -> fciPosition.getId().equals(fciPositionId));
        fciRegulationRepository.save(fciRegulation);
        return fciPositionId;
    }

    @Override
    public FCIPosition findFCIPositionById(String fciRegulationSymbol, Integer fciPositionId) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        return fciRegulation.getPositions().stream().filter(p -> p.getId().equals(fciPositionId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_POSITION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol, fciPositionId)));
    }

    @Override
    public FCIPositionVO findFCIPositionVOById(String fciRegulationSymbol, Integer fciPositionId) throws Exception {
        FCIPosition fciPosition = findFCIPositionById(fciRegulationSymbol, fciPositionId);
        return createFCIPositionVO(fciRegulationSymbol, fciPosition);
    }

    @Override
    public FCIPositionVO findFCIPositionVOByIdRefreshed(String fciRegulationSymbol, Integer fciPositionId) throws Exception {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        FCIPosition fciPosition = findFCIPositionById(fciRegulationSymbol, fciPositionId);
        List<FCISpeciePosition> fciSpeciePositionList = updateCurrentMarketPriceToPosition(fciPosition, true);
        fciPosition.updateMarketPosition(fciSpeciePositionList);
        fciPosition.setOverview(fciSpeciePositionList);

        fciRegulation.getPositions().stream()
                .filter(position -> position.getId().equals(fciPositionId))
                .findFirst()
                .ifPresent(position -> {
                    position.setUpdatedMarketPosition(fciPosition.getUpdatedMarketPosition());
                    position.setOverview(fciSpeciePositionList);
                });
        fciRegulationRepository.save(fciRegulation);

        return createFCIPositionVO(fciRegulationSymbol, fciPosition);
    }

    public List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition, Boolean refresh) throws Exception {
        marketHttpService.updateCurrentMarketPrices();
        return updateCurrentMarketPriceToPosition(fciPosition);
    }
    @Override
    public List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition) throws Exception {
        List<FCISpecieType> fciSpecieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();

        List<FCISpeciePosition> updatedFciSpeciePositions = new ArrayList<>();
        FCIPosition.getSpeciePositions(fciPosition, false).forEach(fciSpeciePosition -> {
            if (isUpdatable(fciSpecieTypes, fciSpeciePosition.getFciSpecieType())) {
                MarketResponse marketResponse = marketHttpService.getMarketResponses().stream()
                        .filter(response -> fciSpeciePosition.getSymbol().equals(response.getMarketSymbol()))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format(
                                ExceptionMessage.POSITION_CONTAINS_NOT_RETRIEVED_SPECIE.msg, fciSpeciePosition.getSymbol())));
                fciSpeciePosition.setCurrentMarketPrice(Double.valueOf(marketResponse.getMarketPrice()));
            }
            updatedFciSpeciePositions.add(fciSpeciePosition);
        });

        return updatedFciSpeciePositions;
    }

    @Override
    public List<FCIPositionIdCreatedOnVO> listPositionsByFCIRegulationSymbolIdCreatedOn(String fciRegulationSymbol) throws Exception {
        return listPositionsByFCIRegulationSymbol(fciRegulationSymbol).stream().map(fciPosition ->
                new FCIPositionIdCreatedOnVO(fciPosition.getId(), fciPosition.getTimestamp())).toList();
    }

    @Override
    public Map<String, Integer> listPositionsByFCIRegulationSymbolMonthlyGrouped(String fciRegulationSymbol) {
        List<FCIPosition> fciPositions = listPositionByFCIRegulation(fciRegulationSymbol);
        Map<String, IntSummaryStatistics> groupedPositionsPerMonth = fciPositions.stream().map(FCIPosition::getDateCreatedOn)
                .collect(Collectors.groupingBy(date -> DateOperationHelper.month(date.getMonth()), Collectors.summarizingInt(x -> 1)));

        return groupedPositionsPerMonth.entrySet().stream().map(entry ->
            Map.entry(entry.getKey(), (int) entry.getValue().getCount()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Integer> listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(String fciRegulationSymbol) {
        Map<String, Integer> sortedMonthMap = new LinkedHashMap<>();
        Map<String, Integer> partialPositionGroupedList = listPositionsByFCIRegulationSymbolMonthlyGrouped(fciRegulationSymbol);
        Map<String, Integer> groupedPositionsPerMonth = DateOperationHelper.months.stream().map(month ->
                        Map.entry(month, partialPositionGroupedList.getOrDefault(month, 0)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Month currentMonth = LocalDate.now().getMonth();
        for (int i = 0; i < 12; i++) {
            String m = currentMonth.minus(i).toString();
            String k = m.substring(0, 1).toUpperCase() + m.substring(1).toLowerCase();
            sortedMonthMap.put(k, groupedPositionsPerMonth.get(k));
        }

        return sortedMonthMap;
    }

    private Boolean isUpdatable(List<FCISpecieType> fciSpecieTypes, String fciSpecieTypeName) {
        return fciSpecieTypes.stream().filter(fciSpecieType -> fciSpecieType.getName().equals(fciSpecieTypeName)).findFirst().orElseThrow().getUpdatable();
    }

    @Override
    public List<FCIPositionVO> listPositionsByFCIRegulationSymbol(String fciRegulationSymbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        List<FCIPosition> positions = fciRegulation.getPositions();
                return positions.stream().map(p -> createFCIPositionVO(fciRegulationSymbol, p)).sorted().collect(Collectors.toList());
    }

    public List<FCIPosition> listPositionByFCIRegulation(String fciRegulationSymbol) {
        FCIRegulation fciRegulation = fciRegulationRepository.findBySymbol(fciRegulationSymbol)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol)));
        return fciRegulation.getPositions();
    }

    private FCIPositionVO createFCIPositionVO(String fciRegulationSymbol, FCIPosition fciPosition) {
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

    /**
     * Sets associated species to indicated position
     * Associated species are those that are bound to a specie type, in turn to a specie group
     *
     * Position JsonNode is to be changed to be added specie type and specie group, to avoid uploading these values.
     *
     * @param fciPosition incoming position
     */
    private void setAssociatedSpeciesToPosition(FCIPosition fciPosition) {

    }

    private Boolean validatePosition(FCIPosition fciPosition, FCIRegulation fciRegulation) throws Exception {
        List<FCIComposition> fciCompositions = fciRegulation.getComposition();
        List<Integer> fciRegulationSpecieTypeIds = fciCompositions.stream().map(FCIComposition::getFciSpecieTypeId).toList();
        List<FCIPositionCompositionVO> positionComposition = FCIPosition.getPositionComposition(fciPosition, false);
        List<String> incomingNames = positionComposition.stream().map(FCIPositionCompositionVO::getSpecieType).toList();
        List<FCISpecieType> specieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();
        List<String> specieTypeNames = specieTypes.stream().map(FCISpecieType::getName).toList();
        List<String> fciRegulationNames = fciRegulationSpecieTypeIds.stream().map(id ->
                        specieTypes.stream().filter(specieType -> specieType.getFciSpecieTypeId().equals(id)).findAny().orElseThrow())
                .map(FCISpecieType::getName).toList();

        /* All incoming specie types must exist */
        incomingNames.forEach(incomingName -> {
            if (!specieTypeNames.contains(incomingName)) {
                throw new PositionValidationException(String.format(ExceptionMessage.SPECIE_TYPE_DOES_NOT_EXIST.msg, incomingName));
            }
        });

        /* All incoming specie types must be included in FCI Regulation */
        incomingNames.forEach(incomingName -> {
            if (!fciRegulationNames.contains(incomingName)) {
                throw new PositionValidationException(String.format(ExceptionMessage.SPECIE_TYPE_DOES_NOT_BELONG_TO_REGULATION.msg, incomingName, fciRegulation.getSymbol()));
            }
        });

        /* Uploaded excel position file must contains all expected column names */
        EnumSet<PositionFieldNameEnum> positionFieldNames = EnumSet.allOf(PositionFieldNameEnum.class);
        positionFieldNames.forEach(fieldName -> {
            if (!fciPosition.getJsonPosition().contains(fieldName.name())) {
                throw new PositionValidationException(String.format(ExceptionMessage.INVALID_POSITION_FIELD_NAME.msg, fieldName.name()));
            }
        });

        /* All species in position must be valid market symbols */
        List<FCISpeciePosition> speciePositions = FCIPosition.getSpeciePositions(fciPosition, false);
        List<String> marketSpecieSymbols = marketHttpService.getAllWorkableSpecies().stream().map(MarketResponse::getMarketSymbol).toList();
        List<FCISpeciePosition> fciSpeciePositions = speciePositions.stream().filter(speciePosition -> !speciePosition.getSymbol().equals(SpecieTypeGroupEnum.Cash.name())).toList();
        fciSpeciePositions.forEach(speciePosition -> {
            if (!marketSpecieSymbols.contains(speciePosition.getSymbol())) {
                throw new PositionValidationException(String.format(ExceptionMessage.INVALID_POSITION_SPECIE_SYMBOL.msg, speciePosition.getSymbol()));
            }
        });

        /* All species in position must be associated to a specie type */

        /* All specie groups in position must be defined */
        List<SpecieTypeGroupDto> specieTypeGroups = fciSpecieTypeGroupService.listFCISpecieTypeGroups();
        List<String> groupNames = specieTypeGroups.stream().map(SpecieTypeGroupDto::getName).toList();
        speciePositions.forEach(speciePosition -> {
            if (!groupNames.contains(speciePosition.getFciSpecieGroup())) {
                throw new PositionValidationException(String.format(ExceptionMessage.INVALID_POSITION_SPECIE_GROUP.msg, speciePosition.getFciSpecieGroup()));
            }
        });

        /* Cash Specie Type must be included in Position */
        speciePositions.stream().filter(specieInPosition -> specieInPosition.getSymbol().equals(SpecieTypeGroupEnum.Cash.name())).findFirst()
                .orElseThrow(() -> new PositionValidationException(ExceptionMessage.CASH_SPECIE_TYPE_NOT_INCLUDED_POSITION.msg));

        return true;
    }
}
