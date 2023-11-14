package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPercentageVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationSymbolAndNameVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Comprehends all {@link FCIRegulation} operations
 */
@RestController
@RequestMapping(path = "/api/v1")
public class FCIRegulationController {

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;


    @PostMapping("/fci")
    public FCIRegulation createFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationCRUDService.createFCIRegulation(fciRegulation);
    }

    @DeleteMapping("/fci/{fciRegulationSymbol}")
    public FCIRegulation deleteFCIRegulation(@PathVariable String fciRegulationSymbol) {
        return fciRegulationCRUDService.deleteFCIRegulation(fciRegulationSymbol);
    }

    @PutMapping("/fci/{fciRegulationSymbol}")
    public FCIRegulation updateFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationCRUDService.updateFCIRegulation(fciRegulation);
    }

    @GetMapping("/fci/{fciRegulationSymbol}")
    public FCIRegulation getFCIRegulation(@PathVariable String fciRegulationSymbol) {
        return fciRegulationCRUDService.findFCIRegulation(fciRegulationSymbol);
    }

    @GetMapping("/fci")
    public List<FCIRegulation> listFCIRegulations() {
        return fciRegulationCRUDService.listFCIRegulations();
    }

    @GetMapping("/fci/symbol-name")
    public List<FCIRegulationSymbolAndNameVO> listFCIRegulationSymbols() {
        return fciRegulationCRUDService.listFCIRegulationSymbolsAndNames();
    }


    @GetMapping("/fci/{fciRegulationSymbol}/regulation-percentages")
    public List<FCIPercentageVO> getFCIRegulationPercentages(@PathVariable String fciRegulationSymbol) {
        return fciRegulationCRUDService.listFCIRegulationPercentages(fciRegulationSymbol);
    }
}
