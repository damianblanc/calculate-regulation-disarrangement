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
    public FCISpecieTypeGroup createFCISpecieTypeGroup(FCISpecieTypeGroup FCISpecieTypeGroup) {
        return fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
    }

    @Override
    public String deleteFCISpecieTypeGroup(String FCISpecieTypeGroupName) {
        FCISpecieTypeGroup toDelete = fciSpecieTypeGroupRepository.findByName(FCISpecieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieTypeGroupName)));
        fciSpecieTypeGroupRepository.delete(toDelete);
        return FCISpecieTypeGroupName;
    }

    @Override
    public FCISpecieTypeGroup updateFCISpecieTypeGroup(FCISpecieTypeGroup FCISpecieTypeGroup) {
        FCISpecieTypeGroup toUpdate = fciSpecieTypeGroupRepository.findByName(FCISpecieTypeGroup.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieTypeGroup.getName())));
        toUpdate.setName(FCISpecieTypeGroup.getName());
        toUpdate.setDescription(FCISpecieTypeGroup.getDescription());
        toUpdate.setUrl(FCISpecieTypeGroup.getUrl());
        toUpdate.setFciSpecieTypes(FCISpecieTypeGroup.getFciSpecieTypes());
        return fciSpecieTypeGroupRepository.save(toUpdate);
    }

    @Override
    public FCISpecieTypeGroup findFCISpecieTypeGroup(String FCISpecieTypeGroupName) {
        return fciSpecieTypeGroupRepository.findByName(FCISpecieTypeGroupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieTypeGroupName)));
    }

    public List<FCISpecieTypeGroup> listFCISpecieTypeGroups() {
        return fciSpecieTypeGroupRepository.findAll();
    }


    /* FCISpecieType */
    @Override
    public FCISpecieTypeGroup createFCISpecieType(String groupName, FCISpecieType FCISpecieType) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(groupName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, groupName)));
        FCISpecieTypeGroup.getFciSpecieTypes().add(FCISpecieType);
        return fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
    }

    @Override
    public String deleteFCISpecieType(String FCISpecieTypeNameGroup, String FCISpecieTypeName) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(FCISpecieTypeName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieTypeNameGroup)));
        FCISpecieTypeGroup.getFciSpecieTypes().removeIf(sp -> sp.getName().equals(FCISpecieTypeName));
        fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
        return FCISpecieTypeName;
    }

    @Override
    public FCISpecieType updateFCISpecieType(String FCISpecieTypeNameGroup, FCISpecieType FCISpecieType) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(FCISpecieType.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieType.getName())));
        FCISpecieType foundFCISpecieType = FCISpecieTypeGroup.getFciSpecieTypes().stream().filter(sp -> FCISpecieType.getName().equals(sp.getName())).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, FCISpecieTypeNameGroup)));
        foundFCISpecieType.setName(FCISpecieType.getName());
        foundFCISpecieType.setDescription(FCISpecieType.getDescription());
        fciSpecieTypeGroupRepository.save(FCISpecieTypeGroup);
        return foundFCISpecieType;
    }

    @Override
    public FCISpecieType findFCISpecieType(String FCISpecieTypeNameGroup, String FCISpecieTypeName) {
        FCISpecieTypeGroup FCISpecieTypeGroup = fciSpecieTypeGroupRepository.findByName(FCISpecieTypeNameGroup)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND.msg, FCISpecieTypeNameGroup)));
        return FCISpecieTypeGroup.getFciSpecieTypes().stream().filter(FCISpecieType -> FCISpecieType.getName().equals(FCISpecieTypeName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.SPECIE_TYPE_ENTITY_NOT_FOUND.msg, FCISpecieTypeNameGroup)));
    }

    @Override
    public List<FCISpecieType> listFCISpecieTypes(String FCISpecieTypeGroupName) {
        return fciSpecieTypeGroupRepository.findAll().stream()
                .map(FCISpecieTypeGroup::getFciSpecieTypes)
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }
}