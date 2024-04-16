package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieToSpecieTypeVO;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieTypeGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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
    List<SpecieTypeGroupDto> listFCISpecieTypeGroups();

    /* SpecieType */
    /**
     * Creates a {@link SpecieTypeGroupEnum}
     * @Param groupName Specie Type Group symbol representation
     * @Param specieType to be created
     * {@link FCISpecieType} Inherites updatable behavior from its parent bound {@link FCISpecieTypeGroup}
     * Updatable property refers to the ability to take current prices from market and apply them to a position
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
     * Finds a {@link FCISpecieType} indicated by its name
     */
    FCISpecieType findFCISpecieType(String specieTypeNameGroup, String specieTypeName);

    /**
     * Lists all {@link FCISpecieType}
     */
    List<FCISpecieType> listFCISpecieTypes(String FCISpecieTypeGroupName);

    /**
     * Lists all available {@link FCISpecieType} for all {@link SpecieTypeGroupEnum}
     */
    List<FCISpecieType> listFCISpecieTypes();

    /**
     * List all available {@link FCISpecieType} names for all {@link SpecieTypeGroupEnum}
     * @return Available FCISpecieType names
     */
    List<String> listFCISpecieTypeNames();

    /**
     * Given a list of {@link FCISpecieType} names indicates whether all are included in available specie types
     */
    Boolean allMatchInSpecieTypes(List<String> incomingSpecieTypeNames);

    /**
     * Lists updatable {@link FCISpecieType} for all {@link SpecieTypeGroupEnum}
     */
    List<FCISpecieType> listUpdatableSpecieTypes();

    /**
     * Lists not updatable {@link FCISpecieType} for all {@link SpecieTypeGroupEnum}
     * There are special cases for specie types that cannot take a current price from market
     * I/E: Cash specie type
     */
    List<FCISpecieType> listNotUpdatableSpecieTypes();

    /**
     * Creates an association between a specie and a specie type within a specie group
     */
    SpecieToSpecieTypeVO createSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol);

    /**
     * Deletes, unbind an association between a specie and a specie type within a specie group
     */
    void deleteSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol);

    /**
     * Changes a specie to other specie type within a specie group
     */
    SpecieToSpecieTypeVO updateSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName, String specieSymbol);

    /**
     * Retrieves a search association between a specie and a specie type within a specie group
     */
    FCISpecieToSpecieType findSpecieToSpecieTypeAssociation(String specieSymbol);

    /**
     * Retrieves a search association between a specie and a specie type within a specie group
     */
    Optional<FCISpecieToSpecieType> findOptionalSpecieToSpecieTypeAssociation(String specieSymbol);

    /**
     * List all available associations between a specie and a specie type within a specie group
     */
    List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociation(String specieTypeGroupName);

    /**
     * List all available associations between a specie and a specie type within a specie group filtered by indicated specie type
     */
    List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociation(String specieTypeGroupName, String specieTypeName);

    /**
     * List all persisted associations between a specie and a specie type with specie group
     */
    List<SpecieToSpecieType> listAllSpecieToSpecieTypeAssociations();

    /**
     * Persists received species / specie type associations in order to be used as a base to receive further positions
     * @param fciSpecieToSpecieTypes contains species of indicated {@link FCISpecieTypeGroup} defined specie type groups
     */
    List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociations(String specieTypeGroupName, List<FCISpecieToSpecieType> fciSpecieToSpecieTypes);

    /**
     * Persists received species / specie type associations in order to be used as a base to receive further positions
     * @param fciSpecieToSpecieTypes contains species for all defined specie type groups
     */
    List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociations(List<FCISpecieToSpecieType> fciSpecieToSpecieTypes);
}