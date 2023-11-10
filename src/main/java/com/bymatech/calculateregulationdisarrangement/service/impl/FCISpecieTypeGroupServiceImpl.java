package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.repository.FCISpecieTypeGroupRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FCISpecieTypeGroupServiceImpl implements FCISpecieTypeGroupService {

    @Autowired
    private FCISpecieTypeGroupRepository fciSpecieTypeGroupRepository;

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

    public List<FCISpecieTypeGroup> listFCISpecieTypeGroups() {
        return fciSpecieTypeGroupRepository.findAll();
    }


    /* FCISpecieType */
    @Override
    public FCISpecieTypeGroup createFCISpecieType(String groupName, FCISpecieType fciSpecieType) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(groupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, groupName)));
        FCISpecieTypeGroup.getFciSpecieTypes().add(fciSpecieType);
        return fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
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
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<FCISpecieType> listFCISpecieTypes() {
        return fciSpecieTypeGroupRepository.findAll().stream()
                .map(FCISpecieTypeGroup::getFciSpecieTypes)
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<FCISpecieType> listUpdatableSpecieTypes() {
        return listFCISpecieTypes().stream().filter(FCISpecieType::getUpdatable).toList();
    }

    @Override
    public List<FCISpecieType> listNotUpdatableSpecieTypes() {
        return listFCISpecieTypes().stream().filter(fciSpecieType -> !fciSpecieType.getUpdatable()).toList();
    }
}