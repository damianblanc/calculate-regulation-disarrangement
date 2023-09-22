package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.BymaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BymaMarketController {

    @Autowired
    private BymaService bymaService;

    @GetMapping("/cedears")
    public List<BymaCedearResponse> getCedears() throws Exception {
        return bymaService.getCedears(BymaCedearAuthBean.create());
    }

    @GetMapping("/bonds")
    public BymaBondResponse getBonds() throws Exception {
        return bymaService.getBonds(BymaBondAuthBean.create(1));
    }

    @GetMapping("/bonds/prices")
    public List<SpecieCurrentPriceVO> getBondPrices() throws Exception {
        return bymaService.getBondsOrderByPriceVO(OrderType.DESC);
    }
}
