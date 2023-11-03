package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    @DeleteMapping("/fci/{symbol}")
    public FCIRegulation deleteFCIRegulation(@PathVariable String symbol) {
        return fciRegulationCRUDService.deleteFCIRegulation(symbol);
    }

    @PutMapping("/fci/{symbol}")
    public FCIRegulation updateFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationCRUDService.updateFCIRegulation(fciRegulation);
    }

    @GetMapping("/fci/{symbol}")
    public FCIRegulation getFCIRegulation(@PathVariable String symbol) {
        return fciRegulationCRUDService.findFCIRegulation(symbol);
    }

    @GetMapping("/fci")
    public Set<FCIRegulation> listFCIRegulations() {
        return fciRegulationCRUDService.listFCIRegulations();
    }
}
