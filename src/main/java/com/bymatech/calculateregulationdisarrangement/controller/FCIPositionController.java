package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionIdCreatedOnVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class FCIPositionController {

    @Autowired
    private FCIPositionService fciPositionService;

    @PostMapping("/fci/{fciRegulationSymbol}/position")
    public FCIPositionVO createFCIPosition(@PathVariable String fciRegulationSymbol, @RequestBody FCIPosition fciPosition) throws Exception {
        return fciPositionService.createFCIPosition(fciRegulationSymbol, fciPosition);
    }

    @DeleteMapping("/fci/{fciRegulationSymbol}/position/{fciPositionId}")
    public Integer deleteFCIPosition(@PathVariable String fciRegulationSymbol, @PathVariable Integer fciPositionId) throws Exception {
        return fciPositionService.deleteFCIPosition(fciRegulationSymbol, fciPositionId);
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/{id}")
    public FCIPositionVO findFCIPositionByFCIRegulationSymbol(@PathVariable String fciRegulationSymbol, @PathVariable String id) throws Exception {
        return fciPositionService.findFCIPositionVOById(fciRegulationSymbol, Integer.valueOf(id));
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/{id}/refresh")
    public FCIPositionVO findFCIPositionByFCIRegulationSymbolRefreshed(@PathVariable String fciRegulationSymbol, @PathVariable String id) throws Exception {
        return fciPositionService.findFCIPositionVOByIdRefreshed(fciRegulationSymbol, Integer.valueOf(id));
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String fciRegulationSymbol) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(fciRegulationSymbol);
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/{positionId}/filtered")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbolFiltered(@PathVariable String fciRegulationSymbol, @PathVariable String positionId) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(fciRegulationSymbol)
                .stream().filter(p -> Integer.parseInt(positionId) == p.getId()).toList();
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/page/{pageNumber}")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String fciRegulationSymbol, @PathVariable Integer pageNumber) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(fciRegulationSymbol)
                .stream().skip(Constants.begin(pageNumber)).limit(Constants.PAGE_SIZE).toList();
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/page/{pageNumber}/page_size/{pageSize}")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbol(@PathVariable String fciRegulationSymbol, @PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        return fciPositionService.listPositionsByFCIRegulationSymbol(fciRegulationSymbol)
                .stream().skip(Constants.begin(pageNumber, pageSize)).limit(pageSize).toList();
    }

    @GetMapping("/fci/{fciRegulationSymbol}/position/from/{fromDate}/to/{toDate}/page/{pageNumber}")
    public List<FCIPositionVO> listFCIPositionsByFCIRegulationSymbolFiltered(@PathVariable String fciRegulationSymbol,
                                                                             @PathVariable String fromDate,
                                                                             @PathVariable String toDate,
                                                                             @PathVariable Integer pageNumber) throws Exception {
        return fciPositionService.listPositionsByFCIRegulationSymbol(fciRegulationSymbol)
                .stream().filter(p -> DateOperationHelper.isInRange(p.getTimestamp(), fromDate, toDate))
                .skip(Constants.begin(pageNumber)).limit(Constants.PAGE_SIZE).toList();
    }

    @GetMapping("/fci/{symbol}/position/id-created-on")
    public List<FCIPositionIdCreatedOnVO> listFCIPositionsByFCIRegulationSymbolIdCreatedOn(@PathVariable String symbol) throws Exception {
        return fciPositionService.listPositionsByFCIRegulationSymbolIdCreatedOn(symbol);
    }
}
