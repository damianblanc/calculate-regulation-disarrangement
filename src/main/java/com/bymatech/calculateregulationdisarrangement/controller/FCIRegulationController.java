package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCICompositionVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationSymbolAndNameVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
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
    private FCIRegulationService fciRegulationService;


    @PostMapping("/fci")
    public FCIRegulationVO createFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationService.createFCIRegulation(fciRegulation);
    }

    @DeleteMapping("/fci/{fciRegulationSymbol}")
    public String deleteFCIRegulation(@PathVariable String fciRegulationSymbol) {
        return fciRegulationService.deleteFCIRegulation(fciRegulationSymbol);
    }

    @PutMapping("/fci/{fciRegulationSymbol}")
    public FCIRegulationVO updateFCIRegulation(@RequestBody FCIRegulation fciRegulation) {
        return fciRegulationService.updateFCIRegulation(fciRegulation);
    }

    @GetMapping("/fci/{fciRegulationSymbol}")
    public FCIRegulationVO getFCIRegulation(@PathVariable String fciRegulationSymbol) {
        return fciRegulationService.findFCIRegulation(fciRegulationSymbol);
    }

    @GetMapping("/fci/{fciRegulationSymbol}/filtered")
    public List<FCIRegulationVO> getFCIRegulationFiltered(@PathVariable String fciRegulationSymbol) {
        try {
            if (fciRegulationSymbol.isEmpty()) return listFCIRegulations();
            return List.of(fciRegulationService.findFCIRegulation(fciRegulationSymbol));
        } catch(final Exception e) {
                return List.of();
        }
    }

    @GetMapping("/fci")
    public List<FCIRegulationVO> listFCIRegulations() {
        return fciRegulationService.listFCIRegulations().stream().sorted().toList();
    }

    @GetMapping("/fci/page/{pageNumber}/page_size/{pageSize}")
    public List<FCIRegulationVO> listFCIRegulationsFiltered(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        return fciRegulationService.listFCIRegulations()
                .stream().sorted().skip(Constants.begin(pageNumber, pageSize)).limit(pageSize).toList();
    }

    @GetMapping("/fci/regulations")
    public List<FCIRegulationSymbolAndNameVO> listFCIRegulationSymbols() {
        return fciRegulationService.listFCIRegulationSymbolsAndNames();
    }


    @GetMapping("/fci/{fciRegulationSymbol}/regulation-percentages")
    public List<FCICompositionVO> getFCIRegulationPercentages(@PathVariable String fciRegulationSymbol) {
        return fciRegulationService.listFCIRegulationPercentages(fciRegulationSymbol);
    }
}
