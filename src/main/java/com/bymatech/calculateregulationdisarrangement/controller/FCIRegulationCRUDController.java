package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FCIRegulationCRUDController {

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

    @PutMapping("/fci")
    public FCIRegulation updateFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationCRUDService.updateFCIRegulation(fciRegulation);
    }

    @GetMapping("/fci/{symbol}")
    public FCIRegulation getFCIRegulation(@PathVariable String symbol) {
        return fciRegulationCRUDService.findFCIRegulation(symbol);
    }

    @GetMapping("/fci")
    public List<FCIRegulation> listFCIRegulations() {
        return fciRegulationCRUDService.listFCIRegulations();
    }
}
