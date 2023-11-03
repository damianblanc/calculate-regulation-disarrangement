package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comprehends CRUD operations over {@link FCISpecieTypeGroup} and {@link SpecieTypeGroupEnum}
 */
@Service
public interface FCISpecieTypeGroupService {

    /* FCISpecieTypeGroup */
    /**
     * Creates a {@link FCISpecieTypeGroup}
     * @param FCISpecieTypeGroup to be created
     */
    FCISpecieTypeGroup createFCISpecieTypeGroup(FCISpecieTypeGroup FCISpecieTypeGroup);

    /**
     * Deletes a {@link FCISpecieTypeGroup} indicated by its name
     */
    String deleteFCISpecieTypeGroup(String FCISpecieTypeGroupName);

    /**
     * Updates a {@link FCISpecieTypeGroup} by incoming state
     * @param FCISpecieTypeGroup to be updated
     */
    FCISpecieTypeGroup updateFCISpecieTypeGroup(FCISpecieTypeGroup FCISpecieTypeGroup);

    /**
     * Finds a {@link FCISpecieTypeGroup} indicated by its name
     */
    FCISpecieTypeGroup findFCISpecieTypeGroup(String FCISpecieTypeGroupName);

    /**
     * Retrieves available Specie Type Groups
     */
    List<FCISpecieTypeGroup> listFCISpecieTypeGroups();

    /* SpecieType */
    /**
     * Creates a {@link SpecieTypeGroupEnum}
     * @Param groupName Specie Type Group symbol representation
     * @Param specieType to be created
     */
    FCISpecieTypeGroup createFCISpecieType(String groupName, FCISpecieType FCISpecieType);

    /**
     * Deletes a {@link SpecieTypeGroupEnum} indicated by its name
     */
    String deleteFCISpecieType(String groupName, String specieTypeName);

    /**
     * Updates a {@link SpecieTypeGroupEnum} by incoming state
     * @param fciSpecieType to be updated
     */
    FCISpecieType updateFCISpecieType(String FCISpecieTypeNameGroup, FCISpecieType fciSpecieType);

    /**
     * Finds a {@link SpecieTypeGroupEnum} indicated by its name
     */
    FCISpecieType findFCISpecieType(String specieTypeNameGroup, String specieTypeName);

    /**
     * List all {@link SpecieTypeGroupEnum}
     */
    List<FCISpecieType> listFCISpecieTypes(String FCISpecieTypeGroupName);

    /**
     * List all available {@link FCISpecieType} for all {@link SpecieTypeGroupEnum}
     */
    List<FCISpecieType> listFCISpecieTypes();
}