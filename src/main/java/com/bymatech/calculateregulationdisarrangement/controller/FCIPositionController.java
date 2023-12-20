package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionIdCreatedOnVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FCIPositionController {

    @Autowired
    private FCIPositionService fciPositionService;

    @PostMapping("/fci/{symbol}/position")
    public FCIPositionVO createFCIPosition(@PathVariable String symbol, @RequestBody FCIPosition fciPosition) throws Exception {
        return fciPositionService.createFCIPosition(symbol, fciPosition);
    }

    @GetMapping("/fci/{symbol}/position/{id}")
    public FCIPositionVO findFCIPositionByFCIRegulationSymbol(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciPositionService.findFCIPositionVOById(symbol, Integer.valueOf(id));
    }

    @GetMapping("/fci/{symbol}/position/{id}/refresh")
    public FCIPositionVO findFCIPositionByFCIRegulationSymbolRefreshed(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciPositionService.findFCIPositionVOByIdRefreshed(symbol, Integer.valueOf(id));
    }

    @GetMapping("/fci/{symbol}/position")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String symbol) throws Exception {
        return fciPositionService.listPositionsByFCIRegulationSymbol(symbol);
    }

    @GetMapping("/fci/{symbol}/position/page/{pageNumber}")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String symbol, @PathVariable Integer pageNumber) throws Exception {
        return fciPositionService.listPositionsByFCIRegulationSymbol(symbol)
                .stream().skip(Constants.begin(pageNumber)).limit(Constants.PAGE_SIZE).toList();
    }

    @GetMapping("/fci/{symbol}/position/id-created-on")
    public List<FCIPositionIdCreatedOnVO> listFCIPositionsByFCIRegulationSymbolIdCreatedOn(@PathVariable String symbol) throws Exception {
        return fciPositionService.listPositionsByFCIRegulationSymbolIdCreatedOn(symbol);
    }
}
