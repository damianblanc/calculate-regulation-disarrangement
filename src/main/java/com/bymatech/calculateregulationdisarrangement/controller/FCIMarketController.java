package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FCIMarketController {

    @Autowired
    private MarketHttpService bymaService;

    @GetMapping("/cedears")
    public List<MarketCedearResponse> getCedears() throws Exception {
        return bymaService.getCedears(MarketCedearAuthBean.create());
    }

    @GetMapping("/bonds")
    public MarketBondResponse getBonds() throws Exception {
        return bymaService.getBonds(MarketBondAuthBean.create(1));
    }

    @GetMapping("/bonds/prices")
    public List<SpecieCurrentPriceVO> getBondPrices() throws Exception {
        return bymaService.getBondsOrderByPriceVO(OrderType.DESC);
    }
}
