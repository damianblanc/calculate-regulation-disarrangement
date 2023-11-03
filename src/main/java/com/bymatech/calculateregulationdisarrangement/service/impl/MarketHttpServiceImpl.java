package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.service.http.BymaAPIServiceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class MarketHttpServiceImpl implements MarketHttpService {

    public List<MarketCedearResponse> getCedears(@Body MarketCedearAuthBean marketCedearAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service = BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<List<MarketCedearResponse>> callSync = service.getCedears(marketCedearAuthBean);

        try {
            Response<List<MarketCedearResponse>> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return List.of();
    }

    @Override
    public List<MarketBondResponse.MarketBondResponseElement> getTotalBonds() {
        MarketBondResponse marketBonds = getBonds(MarketBondAuthBean.create(1));
        List<MarketBondResponse.MarketBondResponseElement> bonds = new ArrayList<>(marketBonds.getMarketBondResponses());

        for (int i = 2; i <= marketBonds.getContent().getPageCount(); i++) {
            List<MarketBondResponse.MarketBondResponseElement> marketBondResponses = getBonds(MarketBondAuthBean.create(i)).getMarketBondResponses();
            bonds.addAll(marketBondResponses);
        }

        return bonds;
    }

    @Override
    public MarketBondResponse getBonds(@Body MarketBondAuthBean marketBondAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service = BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<MarketBondResponse> callSync = service.getBonds(marketBondAuthBean);

        try {
            Response<MarketBondResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return MarketBondResponse.create();
    }

    @Override
    public List<MarketBondResponse.MarketBondResponseElement> getBondsOrderedByPrice(OrderType orderType) {
        List<MarketBondResponse.MarketBondResponseElement> bonds =
                getTotalBonds().stream().sorted(Comparator.comparing(MarketBondResponse.MarketBondResponseElement::getMarketPrice)).toList();
        return orderType == OrderType.DESC ? bonds : bonds.stream().sorted(Collections.reverseOrder()).toList();
    }

    @Override
    public List<MarketResponse> getBondsOrderByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition) {
        List<MarketBondResponse.MarketBondResponseElement> marketBondResponseElements = getBondsOrderedByPrice(orderType).stream()
                .filter(bond -> speciesPosition.stream()
                        .anyMatch(specie -> specie.getSymbol().equals(bond.getMarketSymbol())))
                .toList();
        return null;
    }

    @Override
    public List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType) {
        return getBondsOrderedByPrice(orderType).stream()
                .map(bond -> new SpecieCurrentPriceVO(bond.getDescription(), Double.valueOf(bond.getMarketPrice())))
                .toList();
    }

    @Override
    public MarketEquityResponse getEquities(MarketEquityAuthBean marketEquityAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service =
                BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<MarketEquityResponse> callSync = service.getEquities(marketEquityAuthBean);

        try {
            Response<MarketEquityResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return MarketEquityResponse.create();
    }

    @Override
    public List<MarketEquityResponse.MarketEquityResponseElement> getTotalEquities() {
        MarketEquityResponse marketEquities = getEquities(MarketEquityAuthBean.create(1));
        List<MarketEquityResponse.MarketEquityResponseElement> equities = new ArrayList<>(marketEquities.getMarketEquityResponses());

        for (int i = 2; i <= marketEquities.getContent().getPageCount(); i++) {
            List<MarketEquityResponse.MarketEquityResponseElement> marketBondResponses = getEquities(MarketEquityAuthBean.create(i)).getMarketEquityResponses();
            equities.addAll(marketBondResponses);
        }

        return equities;
    }

    @Override
    public List<MarketResponse> getEquityOrderedByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition) {
        return getEquityOrderedByPrice(orderType).stream()
                .filter(equity -> speciesPosition.stream()
                        .anyMatch(specie -> specie.getSymbol().equals(equity.getMarketSymbol())))
                .toList();
    }

    @Override
    public List<MarketResponse> getEquityOrderedByPrice(OrderType orderType) {
        MarketEquityResponse marketEquities = getEquities(MarketEquityAuthBean.create(1));
        List<MarketEquityResponse.MarketEquityResponseElement> equities =
                marketEquities.getMarketEquityResponses().stream().sorted(Comparator.comparing(
                        MarketEquityResponse.MarketEquityResponseElement::getTrade)).toList();
        List<MarketEquityResponse.MarketEquityResponseElement> marketEquityResponseElements = orderType == OrderType.DESC ? equities : equities.stream().sorted(Collections.reverseOrder()).toList();
        return null;
    }
}
