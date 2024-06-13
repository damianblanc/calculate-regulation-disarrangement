package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieToSpecieTypeVO;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieTypeGroupDto;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Comprehends all configuration implementations to be referenced
 */
@RestController
@RequestMapping("/api/v1/component")
public class FCIComponentController {

    @Autowired
    private FCISpecieTypeGroupService fciSpecieTypeGroupService;

    /* FCISpecieTypeGroup */
    @PostMapping("/specie-type-group")
    public FCISpecieTypeGroup createFCISpecieTypeGroup(@RequestBody FCISpecieTypeGroup FCISpecieTypeGroup) {
            return fciSpecieTypeGroupService.createFCISpecieTypeGroup(FCISpecieTypeGroup);
    }

    @DeleteMapping("/specie-type-group/{specie-type-group-name}")
    public String deleteFCISpecieTypeGroup(@PathVariable ("specie-type-group-name") String FCISpecieTypeGroupName) {
        return fciSpecieTypeGroupService.deleteFCISpecieTypeGroup(FCISpecieTypeGroupName);
    }

    @PutMapping("/specie-type-group")
    public FCISpecieTypeGroup updateFCISpecieTypeGroup(@RequestBody FCISpecieTypeGroup FCISpecieTypeGroup) {
        return fciSpecieTypeGroupService.updateFCISpecieTypeGroup(FCISpecieTypeGroup);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}")
    public FCISpecieTypeGroup getFCISpecieTypeGroup(@PathVariable ("specie-type-group-name") String FCISpecieTypeGroupName) {
        return fciSpecieTypeGroupService.findFCISpecieTypeGroup(FCISpecieTypeGroupName);
    }
    @GetMapping("/specie-type-group")
    public List<SpecieTypeGroupDto> listFCISpecieTypeGroups() {
        return fciSpecieTypeGroupService.listFCISpecieTypeGroups();
    }

    /* SpecieType */
    @PostMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public FCISpecieTypeGroup createSpecieType(@PathVariable ("specie-type-group-name") String groupName, @RequestBody FCISpecieType fciSpecieType) {
        return fciSpecieTypeGroupService.createFCISpecieType(groupName, fciSpecieType);
    }

    @DeleteMapping("/specie-type-group/{specie-type-group-name}/specie-type/{specie-type-name}")
    public String deleteSpecieType(@PathVariable ("specie-type-group-name") String fciSpecieTypeGroupName, @PathVariable("specie-type-name") String fciSpecieTypeName) {
        return fciSpecieTypeGroupService.deleteFCISpecieType(fciSpecieTypeGroupName, fciSpecieTypeName);
    }

    @PutMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public FCISpecieType updateSpecieType(@PathVariable ("specie-type-group-name" ) String FCISpecieTypeGroupName, @RequestBody FCISpecieType fciSpecieType) {
        return fciSpecieTypeGroupService.updateFCISpecieType(FCISpecieTypeGroupName, fciSpecieType);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}/specie-type/{specie-type-name}")
    public FCISpecieType getSpecieType(@PathVariable("specie-type-group-name") String FCISpecieTypeGroupName, @PathVariable String specieTypeName) {
        return fciSpecieTypeGroupService.findFCISpecieType(FCISpecieTypeGroupName, specieTypeName);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public List<FCISpecieType> listSpecieTypes(@PathVariable("specie-type-group-name") String fciSpecieTypeGroupName) {
        return fciSpecieTypeGroupService.listFCISpecieTypes(fciSpecieTypeGroupName);
    }

    @GetMapping("/specie-type")
    public List<FCISpecieType> listSpecieTypes() {
        return fciSpecieTypeGroupService.listFCISpecieTypes();
    }

    @PostMapping("specie-to-specie-type-associations")
    public List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociationBySpecieGroupAndSpecieType(@RequestBody List<FCISpecieToSpecieType> associations) {
       return fciSpecieTypeGroupService.createSpecieToSpecieTypeAssociations(associations);
    }

    @PostMapping("specie-to-specie-type-associations/specie-type-group/{specieTypeGroupName}/bind")
    public List<SpecieToSpecieTypeVO> createSpecieToSpecieTypeAssociationBySpecieGroupAndSpecieType(@PathVariable String specieTypeGroupName,
                                                                              @RequestBody List<FCISpecieToSpecieType> associations) {
        return fciSpecieTypeGroupService.createSpecieToSpecieTypeAssociations(specieTypeGroupName, associations);
    }

    @GetMapping("specie-type-group/{specieTypeGroupName}/specie-type/{specieTypeName}/specie/{specieSymbol}/bind")
    public SpecieToSpecieTypeVO createSpecieToSpecieTypeAssociation(@PathVariable String specieTypeGroupName,
                                                                          @PathVariable String specieTypeName, @PathVariable String specieSymbol) {
        return fciSpecieTypeGroupService.upsertSpecieToSpecieTypeAssociation(specieTypeGroupName, specieTypeName, specieSymbol);
    }


    @DeleteMapping("specie-type-group/{specieTypeGroupName}/specie/{specieSymbol}/bind")
    public void deleteSpecieToSpecieTypeAssociation(@PathVariable String specieTypeGroupName, @PathVariable String specieSymbol) {
        fciSpecieTypeGroupService.deleteSpecieToSpecieTypeAssociation(specieTypeGroupName, specieSymbol);
    }

    @GetMapping("/specie-type-group/{specieTypeGroupName}/bind")
    public List<SpecieToSpecieTypeVO> listGroupSpecieToSpecieTypeAssociation(@PathVariable String specieTypeGroupName) {
        return fciSpecieTypeGroupService.listSpecieToSpecieTypeAssociation(specieTypeGroupName);
    }

    @GetMapping("/specie-type-group/{specieTypeGroupName}/bind/page/{pageNumber}")
    public List<SpecieToSpecieTypeVO> listGroupSpecieToSpecieTypeAssociationPaged(@PathVariable String specieTypeGroupName, @PathVariable Integer pageNumber) {
        return fciSpecieTypeGroupService.listSpecieToSpecieTypeAssociation(specieTypeGroupName)
                .stream().skip(Constants.begin(pageNumber)).limit(Constants.PAGE_SIZE).toList();
    }

    @GetMapping("/specie-type-group/{specieTypeGroupName}/specie-type/{specieTypeName}/bind")
    public List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociation(@PathVariable String specieTypeGroupName, @PathVariable String specieTypeName) {
        return fciSpecieTypeGroupService.listSpecieToSpecieTypeAssociation(specieTypeGroupName, specieTypeName);
    }

    @GetMapping("/specie-type-group/{specieTypeGroupName}/specie-type/{specieTypeName}/bind/page/{pageNumber}")
    public List<SpecieToSpecieTypeVO> listSpecieToSpecieTypeAssociationPaged(@PathVariable String specieTypeGroupName, @PathVariable String specieTypeName, @PathVariable Integer pageNumber) {
        return fciSpecieTypeGroupService.listSpecieToSpecieTypeAssociation(specieTypeGroupName, specieTypeName)
                .stream().skip(Constants.begin(pageNumber)).limit(Constants.PAGE_SIZE).toList();
    }
}