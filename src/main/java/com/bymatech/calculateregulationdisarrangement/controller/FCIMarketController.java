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
@RequestMapping("/api/v1/market/")
public class FCIMarketController {

    @Autowired
    private MarketHttpService marketService;

    @GetMapping("/cedears")
    public MarketCedearResponse getCedears() throws Exception {
        return marketService.getCedears(MarketCedearAuthBean.create(1));
    }

    @GetMapping("/bonds")
    public MarketBondResponse getBonds() throws Exception {
        return marketService.getBonds(MarketBondAuthBean.create(1));
    }

    @GetMapping("/bonds/prices")
    public List<SpecieCurrentPriceVO> getBondPrices() throws Exception {
        return marketService.getBondsOrderByPriceVO(OrderType.DESC);
    }
}
