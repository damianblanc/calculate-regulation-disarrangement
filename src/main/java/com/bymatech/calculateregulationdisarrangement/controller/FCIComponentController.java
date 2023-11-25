package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.dto.SpecieTypeGroupDto;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
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
    public String deleteSpecieType(@PathVariable ("specie-type-group-name") String FCISpecieTypeGroupName, @PathVariable String specieTypeName) {
        return fciSpecieTypeGroupService.deleteFCISpecieType(FCISpecieTypeGroupName, specieTypeName);
    }

    @PutMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public FCISpecieType updateSpecieType(@PathVariable ("specie-type-group-name" )String FCISpecieTypeGroupName, @RequestBody FCISpecieType fciSpecieType) {
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
}