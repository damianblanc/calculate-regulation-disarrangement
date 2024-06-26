package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.exception.MarketResponseException;
import com.bymatech.calculateregulationdisarrangement.service.MarketHttpService;
import com.bymatech.calculateregulationdisarrangement.service.http.BymaAPIServiceGenerator;
import com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
        BymaHttpService service = BymaAPIServiceGenerator.createService(BymaHttpService.class);
        Call<List<MarketCedearResponse>> callSync = service.getCedears(marketCedearAuthBean);

        try {
            Response<List<MarketCedearResponse>> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return List.of(MarketCedearResponse.create());
    }

    public List<MarketCedearResponse> getTotalCedears() {
        List<MarketCedearResponse> marketCedears = getCedears(MarketCedearAuthBean.create(1));
        List<MarketCedearResponse> marketCedearResponses =
            new HashSet<>(marketCedears).stream().peek(cedear -> {
                cedear.setMarketSymbol(cedear.getSymbol());
                cedear.setMarketPrice(String.valueOf(cedear.getTrade()));
                cedear.setFciSpecieType();
            }).toList();
        List<MarketCedearResponse> cedears = new ArrayList<>(marketCedearResponses.stream().sorted().toList());

        if (cedears.isEmpty())
            throw new MarketResponseException(ExceptionMessage.MARKET_CEDEAR_INFORMATION_NOT_AVAILABLE.msg);

        if (cedears.stream().map(MarketResponse::getMarketPrice).allMatch(marketPrice ->
            Constants.MARKET_UNAVAILABLE_PRICES.equals(Double.parseDouble(marketPrice))))
            throw new MarketResponseException(ExceptionMessage.MARKET_PRICE_NOT_AVAILABLE.msg);

        return cedears;
    }

    @Override
    public MarketBondResponse getBonds(@Body MarketBondAuthBean marketBondAuthBean) {
        BymaHttpService service = BymaAPIServiceGenerator.createService(BymaHttpService.class);
        Call<MarketBondResponse> callSync = service.getBonds(marketBondAuthBean);

        try {
            Response<MarketBondResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return MarketBondResponse.create();
    }

    public List<MarketBondResponse.MarketBondResponseElement> getTotalBonds() {
        MarketBondResponse marketBonds = getBonds(MarketBondAuthBean.create(1));
        List<MarketBondResponse.MarketBondResponseElement> bonds = new ArrayList<>();

        for (int i = 1; i <= marketBonds.getContent().getPageCount(); i++) {
            List<MarketBondResponse.MarketBondResponseElement> marketBondResponses =
                getBonds(MarketBondAuthBean.create(i)).getMarketBondResponses().stream().peek(bond -> {
                        bond.setMarketSymbol(bond.getSymbol());
                        bond.setMarketPrice(bond.getPrice());
                        bond.setFciSpecieType();
                    }).toList();
            bonds.addAll(new HashSet<>(marketBondResponses));
        }

        if (bonds.isEmpty())
            throw new MarketResponseException(ExceptionMessage.MARKET_BOND_INFORMATION_NOT_AVAILABLE.msg);

        if (bonds.stream().map(MarketResponse::getMarketPrice).allMatch(marketPrice ->
                Constants.MARKET_UNAVAILABLE_PRICES.equals(Double.parseDouble(marketPrice))))
            throw new MarketResponseException(ExceptionMessage.MARKET_PRICE_NOT_AVAILABLE.msg);

        return bonds.stream().sorted().toList();
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
    public MarketEquityResponse getLeadingEquities(MarketEquityAuthBean marketEquityAuthBean) {
        BymaHttpService service =
                BymaAPIServiceGenerator.createService(BymaHttpService.class);
        Call<MarketEquityResponse> callSync = service.getLeadingEquities(marketEquityAuthBean);

        try {
            Response<MarketEquityResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return MarketEquityResponse.create();
    }

    @Override
    public MarketEquityResponse getGeneralEquities(MarketEquityAuthBean marketEquityAuthBean) {
        BymaHttpService service =
                BymaAPIServiceGenerator.createService(BymaHttpService.class);
        Call<MarketEquityResponse> callSync = service.getGeneralEquities(marketEquityAuthBean);

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
        MarketEquityResponse marketLeadingEquities = getLeadingEquities(MarketEquityAuthBean.create(1));
        List<MarketEquityResponse.MarketEquityResponseElement> equities = new ArrayList<>();

        if (Objects.nonNull(marketLeadingEquities.getContent())) {
            for (int i = 1; i <= marketLeadingEquities.getContent().getPageCount(); i++) {
                List<MarketEquityResponse.MarketEquityResponseElement> marketEquityResponses =
                    getLeadingEquities(MarketEquityAuthBean.create(i)).getMarketEquityResponses()
                        .stream().peek(equity -> {
                            equity.setMarketSymbol(equity.getSymbol());
                            equity.setMarketPrice(equity.getTrade());
                        }).toList();
                equities.addAll(new HashSet<>(marketEquityResponses));
            }
        }

        MarketEquityResponse marketGeneralEquities = getGeneralEquities(MarketEquityAuthBean.create(1));
        if (Objects.nonNull(marketGeneralEquities.getContent())) {
            for (int i = 1; i <= marketGeneralEquities.getContent().getPageCount(); i++) {
                List<MarketEquityResponse.MarketEquityResponseElement> marketEquityResponses =
                    getGeneralEquities(MarketEquityAuthBean.create(i)).getMarketEquityResponses()
                        .stream().peek(equity -> {
                            equity.setMarketSymbol(equity.getSymbol());
                            equity.setMarketPrice(equity.getTrade());
                        }).toList();
                equities.addAll(new HashSet<>(marketEquityResponses));
            }
        }

        if (equities.isEmpty())
            throw new MarketResponseException(ExceptionMessage.MARKET_EQUITY_INFORMATION_NOT_AVAILABLE.msg);

        if (equities.stream().map(MarketResponse::getMarketPrice).allMatch(marketPrice ->
                Constants.MARKET_UNAVAILABLE_PRICES.equals(Double.parseDouble(marketPrice))))
            throw new MarketResponseException(ExceptionMessage.MARKET_PRICE_NOT_AVAILABLE.msg);

        return equities.stream().sorted().toList();
    }

    @Override
    public List<MarketResponse> getEquityOrderedByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition) {
        return getEquityOrderedByPrice(orderType).stream()
                .filter(equity -> speciesPosition.stream()
                        .anyMatch(specie -> specie.getSymbol().equals(equity.getMarketSymbol())))
                .toList();
    }

    @Override
    public List<MarketResponse> getCedearsOrderedByPrice(OrderType orderType) {
        List<MarketCedearResponse> marketCedears = getCedears(MarketCedearAuthBean.create(1));
        List<MarketCedearResponse> cedears = marketCedears.stream().sorted(Comparator.comparing(MarketCedearResponse::getTrade)).toList();
        List<MarketCedearResponse> marketCedearResponses = orderType == OrderType.DESC ? cedears :
            cedears.stream().sorted(Collections.reverseOrder()).toList();
        return null;
    }

    @Override
    public List<MarketResponse> getCedearsOrderedByPriceFilteredBySpecieList(OrderType orderType,
        List<FCISpeciePosition> speciesPosition) {
        return getEquityOrderedByPrice(orderType).stream()
            .filter(equity -> speciesPosition.stream()
                .anyMatch(specie -> specie.getSymbol().equals(equity.getMarketSymbol())))
            .toList();
    }

    @Override
    public List<MarketResponse> getEquityOrderedByPrice(OrderType orderType) {
        MarketEquityResponse marketEquities = getLeadingEquities(MarketEquityAuthBean.create(1));
        List<MarketEquityResponse.MarketEquityResponseElement> equities =
                marketEquities.getMarketEquityResponses().stream().sorted(Comparator.comparing(
                        MarketEquityResponse.MarketEquityResponseElement::getTrade)).toList();
        List<MarketEquityResponse.MarketEquityResponseElement> marketEquityResponseElements = orderType == OrderType.DESC ? equities : equities.stream().sorted(Collections.reverseOrder()).toList();
        return null;
    }

    @Override
    public List<MarketResponse> getAllWorkableSpecies() {
        ArrayList<MarketResponse> responses = new ArrayList<>();
        responses.addAll(getTotalBonds());
        responses.addAll(getTotalEquities());
        responses.addAll(getTotalCedears());
        return responses;
    }
}
