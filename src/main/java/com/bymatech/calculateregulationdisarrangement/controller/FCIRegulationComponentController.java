package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/component")
public class FCIRegulationComponentController {

    @Autowired
    private FCISpecieTypeGroupService FCISpecieTypeGroupService;

    /* FCISpecieTypeGroup */
    @PostMapping("/specie-type-group")
    public FCISpecieTypeGroup createFCISpecieTypeGroup(@RequestBody FCISpecieTypeGroup FCISpecieTypeGroup) {
        return FCISpecieTypeGroupService.createFCISpecieTypeGroup(FCISpecieTypeGroup);
    }

    @DeleteMapping("/specie-type-group/{specie-type-group-name}")
    public String deleteFCISpecieTypeGroup(@PathVariable String FCISpecieTypeGroupName) {
        return FCISpecieTypeGroupService.deleteFCISpecieTypeGroup(FCISpecieTypeGroupName);
    }

    @PutMapping("/specie-type-group")
    public FCISpecieTypeGroup updateFCISpecieTypeGroup(@RequestBody FCISpecieTypeGroup FCISpecieTypeGroup) {
        return FCISpecieTypeGroupService.updateFCISpecieTypeGroup(FCISpecieTypeGroup);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}")
    public FCISpecieTypeGroup getFCISpecieTypeGroup(@PathVariable String FCISpecieTypeGroupName) {
        return FCISpecieTypeGroupService.findFCISpecieTypeGroup(FCISpecieTypeGroupName);
    }
    @GetMapping("/specie-type-group")
    public List<FCISpecieTypeGroup> listFCISpecieTypeGroups() {
        return FCISpecieTypeGroupService.listFCISpecieTypeGroups();
    }

    /* SpecieType */
    @PostMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public FCISpecieTypeGroup createSpecieType(@PathVariable String groupName, @RequestBody FCISpecieType fciSpecieType) {
        return FCISpecieTypeGroupService.createFCISpecieType(groupName, fciSpecieType);
    }

    @DeleteMapping("/specie-type-group/{specie-type-group-name}/specie-type/{specie-type-name}")
    public String deleteSpecieType(@PathVariable String FCISpecieTypeGroupName, @PathVariable String specieTypeName) {
        return FCISpecieTypeGroupService.deleteFCISpecieType(FCISpecieTypeGroupName, specieTypeName);
    }

    @PutMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public FCISpecieType updateSpecieType(@PathVariable String FCISpecieTypeGroupName, @RequestBody FCISpecieType fciSpecieType) {
        return FCISpecieTypeGroupService.updateFCISpecieType(FCISpecieTypeGroupName, fciSpecieType);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}/specie-type/{specie-type-name}")
    public FCISpecieType getSpecieType(@PathVariable String FCISpecieTypeGroupName, @PathVariable String specieTypeName) {
        return FCISpecieTypeGroupService.findFCISpecieType(FCISpecieTypeGroupName, specieTypeName);
    }

    @GetMapping("/specie-type-group/{specie-type-group-name}/specie-type")
    public List<FCISpecieType> listSpecieTypes(@PathVariable String FCISpecieTypeGroupName) {
        return FCISpecieTypeGroupService.listFCISpecieTypes(FCISpecieTypeGroupName);
    }
}