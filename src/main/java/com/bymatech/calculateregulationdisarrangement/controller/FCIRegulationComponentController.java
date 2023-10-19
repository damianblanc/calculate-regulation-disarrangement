package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FCIRegulationComponentController {

    @Autowired
    private FCIRegulationComponentService fciRegulationComponentService;

    @GetMapping("/component/specie-types")
    public List<SpecieType> listSpecieTypes() { return fciRegulationComponentService.listSpecieTypes(); }
}
