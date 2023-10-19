package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class FCIPositionController {

    @Autowired
    private FCIPositionService fciPositionService;

    @PostMapping("/fci/{symbol}/position")
    public FCIPosition createFCIPosition(@PathVariable String symbol, @RequestBody FCIPosition fciPosition) {
        return fciPositionService.createFCIPosition(symbol, fciPosition);
    }


    @GetMapping("/fci/{symbol}/position")
    public Set<FCIPosition> listFCIPositionsByFCIRegulationSymbol(@PathVariable String symbol) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(symbol);
    }
}
