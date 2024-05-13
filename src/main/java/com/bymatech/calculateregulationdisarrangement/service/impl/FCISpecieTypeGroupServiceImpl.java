package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieTypePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCISpecieToSpecieTypeRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCISpecieTypeGroupRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.bymatech.calculateregulationdisarrangement.util.NumberFormatHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FCISpecieTypeGroupServiceImpl implements FCISpecieTypeGroupService {

    @Autowired
    private FCISpecieTypeGroupRepository fciSpecieTypeGroupRepository;

    @Autowired
    private FCISpecieToSpecieTypeRepository fciSpecieToSpecieTypeRepository;

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private MarketHttpService marketService;

    @Override
    public FCISpecieTypeGroup createFCISpecieTypeGroup(FCISpecieTypeGroup fciSpecieTypeGroup) {
        List<FCISpecieType> renamedSpecieTypes = fciSpecieTypeGroup.getFciSpecieTypes().stream()
            .peek(fciSpecieType -> fciSpecieType.setName(
                "(" + fciSpecieTypeGroup.getName().charAt(0) + ") " + fciSpecieType.getName())).toList();
        fciSpecieTypeGroup.setFciSpecieTypes(renamedSpecieTypes);
        return fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
    }

    @Override
    public String deleteFCISpecieTypeGroup(String fciSpecieTypeGroupName) {
        FCISpecieTypeGroup toDelete = fciSpecieTypeGroupRepository.findByName(fciSpecieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeGroupName)));
        fciSpecieTypeGroupRepository.delete(toDelete);
        return fciSpecieTypeGroupName;
    }

    @Override
    public FCISpecieTypeGroup updateFCISpecieTypeGroup(FCISpecieTypeGroup fciSpecieTypeGroup) {
        FCISpecieTypeGroup toUpdate = fciSpecieTypeGroupRepository.findByName(fciSpecieTypeGroup.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeGroup.getName())));
        toUpdate.setName(fciSpecieTypeGroup.getName());
        toUpdate.setDescription(fciSpecieTypeGroup.getDescription());
        toUpdate.setFciSpecieTypes(fciSpecieTypeGroup.getFciSpecieTypes());
        return fciSpecieTypeGroupRepository.save(toUpdate);
    }

    @Override
    public FCISpecieTypeGroup findFCISpecieTypeGroup(String fciSpecieTypeGroupName) {
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(
                fciSpecieTypeGroupName)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg,
                    fciSpecieTypeGroupName)));
        List<FCISpecieType> specieTypesWithSpecieQuantity = fciSpecieTypeGroup.getFciSpecieTypes().stream()
            .peek(fciSpecieType -> {
                int speciesInSpecieTypeQuantity = listSpecieToSpecieTypeAssociation(
                    fciSpecieTypeGroupName, fciSpecieType.getName()).size();
                fciSpecieType.setSpecieQuantity(speciesInSpecieTypeQuantity);
            }).toList();
        fciSpecieTypeGroup.setFciSpecieTypes(specieTypesWithSpecieQuantity);
        return fciSpecieTypeGroup;
    }

    public List<SpecieTypeGroupDto> listFCISpecieTypeGroups() {
        return fciSpecieTypeGroupRepository.findAll().stream()
            .map(fciSpecieTypeGroup ->
                    new SpecieTypeGroupDto(fciSpecieTypeGroup.getId(),
                        fciSpecieTypeGroup.getName(), fciSpecieTypeGroup.getDescription(), fciSpecieTypeGroup.getLot(), fciSpecieTypeGroup.getUpdatable(),
                        fciSpecieTypeGroup.getFciSpecieTypes().stream().map(fciSpecieType ->
                            new SpecieTypeDto(fciSpecieType.getFciSpecieTypeId(),
                                    fciSpecieType.getName(),
                                    fciSpecieType.getDescription(), fciSpecieType.getUpdatable(),
                                listSpecieToSpecieTypeAssociation(
                                    fciSpecieTypeGroup.getName(), fciSpecieType.getName()).size())).sorted().toList()))
            .sorted().toList();
    }

    @Override
    public FCISpecieTypeGroup createFCISpecieType(String groupName, FCISpecieType fciSpecieType) {
        List<FCISpecieTypeGroup> fciSpecieTypeGroups = fciSpecieTypeGroupRepository.findAll();
        if (fciSpecieTypeGroups.stream()
            .map(FCISpecieTypeGroup::getFciSpecieTypes)
            .flatMap(fciSpecieTypes -> fciSpecieTypes.stream()
                .map(FCISpecieType::getName)).anyMatch(specieTypeName -> fciSpecieType.getName().equals(specieTypeName))) {
            throw new FailedValidationException(String.format(ExceptionMessage.SPECIE_TYPE_NAME_ALREADY_EXISTS.msg));
        }

        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(groupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, groupName)));
        fciSpecieType.setName("(" + fciSpecieType.getName().charAt(0) + ") " + fciSpecieTypeGroup.getName());
        fciSpecieType.setUpdatable(fciSpecieTypeGroup.getUpdatable());
        fciSpecieTypeGroup.getFciSpecieTypes().add(fciSpecieType);
        return fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
    }

    @Override
    public String deleteFCISpecieType(String fciSpecieTypeNameGroupName, String fciSpecieTypeName) {
        //TODO: What happens with species configured to specie type, are they assigned another specie type or they start to remain unbound?

        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(fciSpecieTypeNameGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeNameGroupName)));

        if (!listSpecieToSpecieTypeAssociation(fciSpecieTypeNameGroupName, fciSpecieTypeName).isEmpty())
            throw new FailedValidationException(String.format(ExceptionMessage.SPECIE_TYPE_CANNOT_BE_DELETED_SPECIES_BOUND.msg, fciSpecieTypeNameGroupName, fciSpecieTypeName));

        fciSpecieTypeGroup.getFciSpecieTypes().removeIf(sp -> sp.getName().equals(fciSpecieTypeName));
        fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
        return fciSpecieTypeName;
    }

    @Override
    public FCISpecieType updateFCISpecieType(String fciSpecieTypeNameGroup, FCISpecieType fciSpecieType) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(fciSpecieType.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieType.getName())));
        FCISpecieType foundFCISpecieType = FCISpecieTypeGroup.getFciSpecieTypes().stream().filter(sp -> fciSpecieType.getName().equals(sp.getName())).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, fciSpecieTypeNameGroup)));
        foundFCISpecieType.setName(fciSpecieType.getName());
        foundFCISpecieType.setDescription(fciSpecieType.getDescription());
        fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
        return foundFCISpecieType;
    }

    @Override
    public FCISpecieType findFCISpecieType(String fciSpecieTypeNameGroup, String fciSpecieTypeName) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(fciSpecieTypeNameGroup)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeNameGroup)));
        return FCISpecieTypeGroup.getFciSpecieTypes().stream().filter(FCISpecieType -> FCISpecieType.getName().equals(fciSpecieTypeName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, fciSpecieTypeNameGroup)));
    }

    @Override
    public List<FCISpecieType> listFCISpecieTypes(String fciSpecieTypeGroupName) {
        return fciSpecieTypeGroupRepository.findByName(fciSpecieTypeGroupName).stream()
                .map(FCISpecieTypeGroup::getFciSpecieTypes)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<FCISpecieType> listFCISpecieTypes() {
        return fciSpecieTypeGroupRepository.findAll().stream()
                .map(FCISpecieTypeGroup::getFciSpecieTypes)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> listFCISpecieTypeNames() {
        return listFCISpecieTypes().stream().map(FCISpecieType::getName).toList();
    }

    public Boolean allMatchInSpecieTypes(List<String> incomingSpecieTypeNames) {
        List<String> availableSpecieTypeNames = listFCISpecieTypeNames();
        return new HashSet<>(availableSpecieTypeNames).containsAll(incomingSpecieTypeNames);
    }

    @Override
    public List<FCISpecieType> listUpdatableSpecieTypes() {
        return listFCISpecieTypes().stream().filter(FCISpecieType::getUpdatable).toList();
    }

    @Override
    public List<FCISpecieType> listNotUpdatableSpecieTypes() {
        return listFCISpecieTypes().stream().filter(fciSpecieType -> !fciSpecieType.getUpdatable()).toList();
    }

    @Override
    public SpecieToSpecieTypeVO upsertSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        StringBuffer referencedPositions = new StringBuffer();

        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(specieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        FCISpecieType f = fciSpecieTypeGroup.getFciSpecieTypes().stream().filter(FCISpecieType -> FCISpecieType.getName().equals(specieTypeName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));

        /* FCI Position Reference Restriction */
        fciSpecieToSpecieTypeRepository.findByName(specieSymbol).ifPresent(bind -> {
            List<FCISpecieToSpecieTypePosition> positionReferences = bind.getPositions();
            if (!positionReferences.isEmpty()) {
                List<FCISpecieToSpecieTypePosition> firstPositionReferences = positionReferences.stream().limit(5).toList();
                firstPositionReferences.forEach(positionReference -> referencedPositions
                    .append("#")
                    .append(positionReference.getFciPositionId()).append(" - ")
                    .append(positionReference.getFciPositionCreatedOn())
                    .append(", "));
                throw new FailedValidationException(String.format(ExceptionMessage.SPECIE_REFERENCED_BY_POSITION.msg,
                    specieSymbol, positionReferences.size(), referencedPositions));
            }
        });

        f.setSpecieQuantity(f.getSpecieQuantity() + 1);
        fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
        FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(FCISpecieToSpecieType.builder().specieSymbol(specieSymbol).fciSpecieType(f).build());
        return new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName(), s.getFciSpecieType().getSpecieQuantity());
    }

    @Override
    public void deleteSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        StringBuffer referencedPositions = new StringBuffer();

        findSpecieToSpecieTypeAssociationOptional(specieSymbol).ifPresent(bind -> {
            List<FCISpecieToSpecieTypePosition> positionReferences = bind.getPositions();
            if (!positionReferences.isEmpty()) {
                List<FCISpecieToSpecieTypePosition> firstPositionReferences = positionReferences.stream().limit(5).toList();
                firstPositionReferences.forEach(positionReference -> referencedPositions
                    .append("#")
                    .append(positionReference.getFciPositionId()).append(" - ")
                    .append(positionReference.getFciPositionCreatedOn())
                    .append(", "));
                throw new FailedValidationException(String.format(ExceptionMessage.SPECIE_REFERENCED_BY_POSITION.msg,
                    specieSymbol, positionReferences.size(), referencedPositions));
             }
            fciSpecieToSpecieTypeRepository.delete(bind);
        });
    }

    /**
     * Updating an association can only change specie type bound for a specie within specie type group
     */
    @Override
    public SpecieToSpecieTypeVO updateSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        FCISpecieToSpecieType specieToSpecieTypeAssociation = findSpecieToSpecieTypeAssociation(specieSymbol);
        specieToSpecieTypeAssociation.setFciSpecieType(findFCISpecieType(specieTypeGroupName, specieTypeName));
        FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(specieToSpecieTypeAssociation);
        return new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName(), s.getFciSpecieType().getSpecieQuantity());
    }

    @Override
    public FCISpecieToSpecieType findSpecieToSpecieTypeAssociation(String specieSymbol) {
        return fciSpecieToSpecieTypeRepository.findBySpecieSymbol(specieSymbol)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ASSOCIATION_ENTITY_NOT_FOUND.msg, specieSymbol)));
    }

    public Optional<FCISpecieToSpecieType> findSpecieToSpecieTypeAssociationOptional(String specieSymbol) {
        return fciSpecieToSpecieTypeRepository.findByName(specieSymbol);
    }

    @Override
    public Optional<FCISpecieToSpecieType> findOptionalSpecieToSpecieTypeAssociation(String specieSymbol) {
        return fciSpecieToSpecieTypeRepository.findBySpecieSymbol(specieSymbol);
    }

    @Override
    public List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociation(String specieTypeGroupName) {
        AtomicInteger index = new AtomicInteger();
        List<FCISpecieToSpecieType> specieToSpecieTypes = new ArrayList<FCISpecieToSpecieType>();
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(specieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        fciSpecieTypeGroup.getFciSpecieTypes().forEach(fciSpecieType -> specieToSpecieTypes.addAll(fciSpecieToSpecieTypeRepository.listBySpecieType(fciSpecieType)));

        if (SpecieTypeGroupEnum.Equity.name().equals(specieTypeGroupName)) {
            return marketService.getTotalEquities().stream().map(specie ->
                    getSpecieToSpecieTypeVO(specieToSpecieTypes, specie.getSymbol(), index)).toList();
        }
        if (SpecieTypeGroupEnum.Cedears.name().equals(specieTypeGroupName)) {
            return marketService.getTotalCedears().stream().map(specie ->
                getSpecieToSpecieTypeVO(specieToSpecieTypes, specie.getSymbol(), index)).toList();
        }
        if (SpecieTypeGroupEnum.Cash.name().equals(specieTypeGroupName)) {
            return List.of(getSpecieToSpecieTypeVO(specieToSpecieTypes, "Cash", index));
        }
        return marketService.getTotalBonds().stream().map(specie ->
                getSpecieToSpecieTypeVO(specieToSpecieTypes, specie.getSymbol(), index)).toList();
    }

    @Override
    public List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName) {
        AtomicInteger index = new AtomicInteger();
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(specieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        FCISpecieType foundFCISpecieType = fciSpecieTypeGroup.getFciSpecieTypes().stream().filter(sp -> specieTypeName.equals(sp.getName())).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, specieTypeName)));
        List<FCISpecieToSpecieType> fciSpecieToSpecieTypes = fciSpecieToSpecieTypeRepository.listBySpecieType(foundFCISpecieType);

        return fciSpecieToSpecieTypes.stream().map(specie ->
            getSpecieToSpecieTypeVO(fciSpecieToSpecieTypes, specie.getSpecieSymbol(), index)).toList();
    }

    @Override
    public List<SpecieToSpecieType> listAllSpecieToSpecieTypeAssociations() {
        List<FCISpecieToSpecieType> fciSpecieToSpecieTypes = fciSpecieToSpecieTypeRepository.findAll();
        List<FCISpecieTypeGroup> fciSpecieTypeGroups = fciSpecieTypeGroupRepository.findAll();
        return fciSpecieToSpecieTypes.stream().map(association ->
                new SpecieToSpecieType(
                        association.getSpecieSymbol(),
                        association.getFciSpecieType().getName(),
                        findFciSpecieTypeGroup(fciSpecieTypeGroups, association).map(FCISpecieTypeGroup::getName).orElse(Strings.EMPTY))).toList();
    }

    @Override
    public List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociations(List<FCISpecieToSpecieType> fciSpecieToSpecieTypes) {
       List<SpecieToSpecieTypeVO> savedAssociations = new ArrayList<>();
       fciSpecieToSpecieTypes.forEach(fciSpecieToSpecieType -> {
           FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(fciSpecieToSpecieType);
           SpecieToSpecieTypeVO specieToSpecieTypeVO = new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName(), s.getFciSpecieType().getSpecieQuantity());
           savedAssociations.add(specieToSpecieTypeVO);
       });
       return savedAssociations;
    }

    @Override
    public Optional<FCISpecieTypeGroup> findSpecieTypeGroup(FCISpeciePosition fciSpeciePosition) {
        return listAllSpecieToSpecieTypeAssociations().stream()
            .filter(bind -> bind.getSpecieSymbol().equals(fciSpeciePosition.getSymbol()))
            .map(bind -> fciSpecieTypeGroupRepository.findByName(bind.getSpecieTypeGroupName()))
            .flatMap(Optional::stream)
            .findFirst().stream().findFirst();
    }

    @Override
    public Optional<FCISpecieToSpecieType> findSpecieToSpecieType(FCISpeciePosition fciSpeciePosition) {
        return fciSpecieToSpecieTypeRepository.findByName(fciSpeciePosition.getSymbol());
    }

    public Integer retrieveGroupLot(FCISpeciePosition fciSpeciePosition) {
        return findSpecieTypeGroup(fciSpeciePosition)
            .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_NOT_BOUND_TO_ANY_SPECIE_TYPE.msg, fciSpeciePosition.getSymbol())))
            .getLot();
    }

    @Override
    public Map<FCISpeciePosition, FCISpecieTypeGroup> listSpecieTypeGroupBindings(List<FCISpeciePosition> fciSpeciePositions) {
        Map<FCISpeciePosition, FCISpecieTypeGroup> bindings = new HashMap<>();
        List<SpecieToSpecieType> specieToSpecieTypes = listAllSpecieToSpecieTypeAssociations();

        fciSpeciePositions.forEach(fciSpeciePosition -> {
            FCISpecieTypeGroup fciSpecieTypeGroup = specieToSpecieTypes.stream()
                .filter(bind -> bind.getSpecieSymbol().equals(fciSpeciePosition.getSymbol()))
                .map(bind -> fciSpecieTypeGroupRepository.findByName(bind.getSpecieTypeGroupName()))
                .flatMap(Optional::stream)
                .findFirst().orElseThrow(() -> new EntityNotFoundException(
                    String.format(ExceptionMessage.SPECIE_NOT_BOUND_TO_ANY_SPECIE_TYPE.msg,
                        fciSpeciePosition.getSymbol())));
            bindings.put(fciSpeciePosition, fciSpecieTypeGroup);
        });

        return bindings;
    }

    public Map<FCISpeciePosition, Optional<SpecieToSpecieType>> listSpecieTypeBindings(List<FCISpeciePosition> fciSpeciePositions) {
        Map<FCISpeciePosition, Optional<SpecieToSpecieType>> bindings = new HashMap<>();
        List<SpecieToSpecieType> specieToSpecieTypes = listAllSpecieToSpecieTypeAssociations();

        fciSpeciePositions.forEach(fciSpeciePosition ->
            bindings.put(fciSpeciePosition, specieToSpecieTypes.stream()
                .filter(bind -> bind.getSpecieSymbol().equals(fciSpeciePosition.getSymbol())).findFirst()));

        return bindings;
    }

    @Override
    public Map<FCISpeciePosition, FCISpecieTypeGroup> listSpecieTypeGroupBindings(Map<FCISpecieType, List<FCISpeciePosition>> fciSpeciePositions) {
        Map<FCISpeciePosition, FCISpecieTypeGroup> bindings = new HashMap<>();
        fciSpeciePositions.values().forEach(fciSpeciePosition -> bindings.putAll(listSpecieTypeGroupBindings(fciSpeciePosition)));
        return bindings;
    }

    @Override
    public List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociations(String specieTypeGroupName, List<FCISpecieToSpecieType> fciSpecieToSpecieTypes) {
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(specieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        List<FCISpecieType> fciSpecieTypes = fciSpecieTypeGroup.getFciSpecieTypes();
        List<SpecieToSpecieTypeVO> savedAssociations = new ArrayList<>();

        /* Verify that all specie types belong to specified group before persisting them */
        fciSpecieToSpecieTypes.forEach(fciSpecieToSpecieType ->
                    fciSpecieTypes.stream()
                            .filter(fciSpecieType -> fciSpecieType.getFciSpecieTypeId().equals(fciSpecieToSpecieType.getFciSpecieType().getFciSpecieTypeId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(String.format(ExceptionMessage.SPECIE_TYPE_DOES_NOT_BELONG_TO_GROUP.msg,
                                    fciSpecieToSpecieType.getFciSpecieType().getFciSpecieTypeId(), fciSpecieToSpecieType.getFciSpecieType().getName(), specieTypeGroupName))));

        fciSpecieToSpecieTypes.forEach(fciSpecieToSpecieType -> {
            FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(fciSpecieToSpecieType);
            SpecieToSpecieTypeVO specieToSpecieTypeVO = new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName(), s.getFciSpecieType().getSpecieQuantity());
            savedAssociations.add(specieToSpecieTypeVO);
        });
        return savedAssociations;
    }

    @NotNull
    private static Optional<FCISpecieTypeGroup> findFciSpecieTypeGroup(List<FCISpecieTypeGroup> fciSpecieTypeGroups, FCISpecieToSpecieType association) {
        return fciSpecieTypeGroups.stream()
                .filter(group -> group.getFciSpecieTypes().stream()
                        .map(FCISpecieType::getFciSpecieTypeId)
                        .anyMatch(fciSpecieTypeId -> association.getFciSpecieType().getFciSpecieTypeId().equals(fciSpecieTypeId)))
                .findFirst();
    }

    private SpecieToSpecieTypeVO getSpecieToSpecieTypeVO(List<FCISpecieToSpecieType> fciSpecieToSpecieTypes, String specie, AtomicInteger index) {
        Optional<FCISpecieToSpecieType> association = findFCISpecieToSpecieType(fciSpecieToSpecieTypes, specie);
        return association.map(specieToSpecieType -> new SpecieToSpecieTypeVO(index.getAndIncrement() + 1, specie,
                        specieToSpecieType.getFciSpecieType().getFciSpecieTypeId(), specieToSpecieType.getFciSpecieType().getName(), specieToSpecieType.getPositions().size()))
                .orElseGet(() -> SpecieToSpecieTypeVO.builder().id(index.getAndIncrement() + 1).specieSymbol(specie).build());
    }

    private Optional<FCISpecieToSpecieType> findFCISpecieToSpecieType(List<FCISpecieToSpecieType> fciSpecieToSpecieTypes, String specieSymbol) {
        return fciSpecieToSpecieTypes.stream().filter(fciSpecieToSpecieType -> fciSpecieToSpecieType.getSpecieSymbol().equals(specieSymbol)).findFirst();
    }
}