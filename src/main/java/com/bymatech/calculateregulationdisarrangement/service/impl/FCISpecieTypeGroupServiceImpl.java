package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.repository.FCISpecieToSpecieTypeRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCISpecieTypeGroupRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
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
    private MarketHttpService marketService;

    /* FCISpecieTypeGroup */
    @Override
    public FCISpecieTypeGroup createFCISpecieTypeGroup(FCISpecieTypeGroup fciSpecieTypeGroup) {
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
        return fciSpecieTypeGroupRepository.findByName(fciSpecieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeGroupName)));
    }

    public List<SpecieTypeGroupDto> listFCISpecieTypeGroups() {
        return fciSpecieTypeGroupRepository.findAll().stream()
            .map(fciSpecieTypeGroup ->
                    new SpecieTypeGroupDto(fciSpecieTypeGroup.getId(),
                        fciSpecieTypeGroup.getName(), fciSpecieTypeGroup.getDescription(), fciSpecieTypeGroup.getUpdatable(),
                        fciSpecieTypeGroup.getFciSpecieTypes().stream().map(fciSpecieType ->
                                new SpecieTypeDto(fciSpecieType.getFciSpecieTypeId(), fciSpecieType.getName(),
                                        fciSpecieType.getDescription(), fciSpecieType.getUpdatable(), fciSpecieType.getSpecieQuantity()))
                                .sorted().toList()))
            .sorted().toList();
    }


    /* FCISpecieType */
    @Override
    public FCISpecieTypeGroup createFCISpecieType(String groupName, FCISpecieType fciSpecieType) {
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(groupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, groupName)));
        fciSpecieType.setUpdatable(fciSpecieTypeGroup.getUpdatable());
        fciSpecieTypeGroup.getFciSpecieTypes().add(fciSpecieType);
        return fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
    }

    @Override
    public String deleteFCISpecieType(String fciSpecieTypeNameGroup, String fciSpecieTypeName) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(fciSpecieTypeName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, fciSpecieTypeNameGroup)));
        FCISpecieTypeGroup.getFciSpecieTypes().removeIf(sp -> sp.getName().equals(fciSpecieTypeName));
        fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
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
    public SpecieToSpecieTypeVO createSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        FCISpecieTypeGroup fciSpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(specieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        FCISpecieType f = fciSpecieTypeGroup.getFciSpecieTypes().stream().filter(FCISpecieType -> FCISpecieType.getName().equals(specieTypeName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, specieTypeGroupName)));
        f.setSpecieQuantity(f.getSpecieQuantity() + 1);
        fciSpecieTypeGroupRepository.save(fciSpecieTypeGroup);
        FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(FCISpecieToSpecieType.builder().specieSymbol(specieSymbol).fciSpecieType(f).build());
        return new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName());
    }

    @Override
    public void deleteSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        FCISpecieToSpecieType specieToSpecieTypeAssociation = findSpecieToSpecieTypeAssociation(specieSymbol);
        fciSpecieToSpecieTypeRepository.delete(specieToSpecieTypeAssociation);
    }

    /**
     * Updating an association can only change specie type bound for an specie within specie type group
     */
    @Override
    public SpecieToSpecieTypeVO updateSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol) {
        FCISpecieToSpecieType specieToSpecieTypeAssociation = findSpecieToSpecieTypeAssociation(specieSymbol);
        specieToSpecieTypeAssociation.setFciSpecieType(findFCISpecieType(specieTypeGroupName, specieTypeName));
        FCISpecieToSpecieType s = fciSpecieToSpecieTypeRepository.save(specieToSpecieTypeAssociation);
        return new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName());
    }

    @Override
    public FCISpecieToSpecieType findSpecieToSpecieTypeAssociation(String specieSymbol) {
        return fciSpecieToSpecieTypeRepository.findBySpecieSymbol(specieSymbol)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ASSOCIATION_ENTITY_NOT_FOUND.msg, specieSymbol)));
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

        if (SpecieTypeGroupEnum.Equity.name().equals(specieTypeGroupName)) {
            return marketService.getTotalEquities().stream().map(specie ->
                    getSpecieToSpecieTypeVO(fciSpecieToSpecieTypes, specie.getSymbol(), index)).toList();
        }
        return marketService.getTotalBonds().stream().map(specie ->
                getSpecieToSpecieTypeVO(fciSpecieToSpecieTypes, specie.getSymbol(), index)).toList();
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
           SpecieToSpecieTypeVO specieToSpecieTypeVO = new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName());
           savedAssociations.add(specieToSpecieTypeVO);
       });
       return savedAssociations;
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
            SpecieToSpecieTypeVO specieToSpecieTypeVO = new SpecieToSpecieTypeVO(s.getId(), s.getSpecieSymbol(), s.getFciSpecieType().getFciSpecieTypeId(), s.getFciSpecieType().getName());
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
                        specieToSpecieType.getFciSpecieType().getFciSpecieTypeId(), specieToSpecieType.getFciSpecieType().getName()))
                .orElseGet(() -> SpecieToSpecieTypeVO.builder().id(index.getAndIncrement() + 1).specieSymbol(specie).build());
    }

    private Optional<FCISpecieToSpecieType> findFCISpecieToSpecieType(List<FCISpecieToSpecieType> fciSpecieToSpecieTypes, String specieSymbol) {
        return fciSpecieToSpecieTypes.stream().filter(fciSpecieToSpecieType -> fciSpecieToSpecieType.getSpecieSymbol().equals(specieSymbol)).findFirst();
    }

}