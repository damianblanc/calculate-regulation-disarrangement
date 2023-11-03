package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class FCIPositionController {

    @Autowired
    private FCIPositionService fciPositionService;

    @PostMapping("/fci/{symbol}/position")
    public FCIPosition createFCIPosition(@PathVariable String symbol, @RequestBody FCIPosition fciPosition) throws JsonProcessingException {
        return fciPositionService.createFCIPosition(symbol, fciPosition);
    }

    @GetMapping("/fci/{symbol}/position/{id}")
    public FCIPosition findFCIPositionByFCIRegulationSymbol(@PathVariable String symbol, @PathVariable String id) {
        return fciPositionService.findFCIPositionById(symbol, Integer.valueOf(id));
    }

    @GetMapping("/fci/{symbol}/position")
    public Set<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String symbol) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(symbol);
    }
}
