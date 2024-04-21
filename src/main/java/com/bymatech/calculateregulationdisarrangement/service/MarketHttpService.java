package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import org.springframework.stereotype.Service;
import retrofit2.http.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public interface MarketHttpService {

    List<MarketResponse> marketResponses = new ArrayList<>();

    List<MarketCedearResponse> getCedears(@Body MarketCedearAuthBean marketCedearAuthBean);

    List<MarketBondResponse.MarketBondResponseElement> getTotalBonds();

    MarketBondResponse getBonds(@Body MarketBondAuthBean marketBondAuthBean);

    List<MarketBondResponse.MarketBondResponseElement> getBondsOrderedByPrice(OrderType orderType);

    List<MarketResponse> getBondsOrderByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition);

    List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType);

    MarketEquityResponse getLeadingEquities(@Body MarketEquityAuthBean marketEquityAuthBean);

    MarketEquityResponse getGeneralEquities(@Body MarketEquityAuthBean marketEquityAuthBean);

    List<MarketEquityResponse.MarketEquityResponseElement> getTotalEquities();

    List<MarketCedearResponse> getTotalCedears();

    List<MarketResponse> getEquityOrderedByPrice(OrderType orderType);

    List<MarketResponse> getEquityOrderedByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition);

    List<MarketResponse> getAllWorkableSpecies();

    /**
     * An Asynchronous fixed delay scheduled task to retrieve current market prices for all Specie Type Groups
     */
//    @Scheduled(fixedDelayString = "${schedule.advice.position.fixed.delay.minutes:5}")
//    @Scheduled(fixedDelayString = "${schedule.advice.position.fixed.delay.seconds:2}000")
    default List<MarketResponse> updateCurrentMarketPrices() {
        //TODO: See that comunication can fail, then do not clear before getting results, test comunicacion
        //TODO: Does returned data does not change after stock round closure? I/E: 17.00, until 11.00 next day?
        //TODO: Avoid going to retrieve data when it becomes constant.
        try {
            marketResponses.clear();

            CompletableFuture<List<MarketBondResponse.MarketBondResponseElement>> futureBondsTask = CompletableFuture.supplyAsync(this::getTotalBonds);
            List<MarketBondResponse.MarketBondResponseElement> bonds = new ArrayList<>();
            while (!futureBondsTask.isDone()) {
                bonds = futureBondsTask.get();

            }
            if (bonds.isEmpty()) {
                throw new IllegalArgumentException(ExceptionMessage.MARKET_BOND_INFORMATION_NOT_AVAILABLE.msg);
            }

            CompletableFuture<List<MarketEquityResponse.MarketEquityResponseElement>> futureEquitiesTask = CompletableFuture.supplyAsync(this::getTotalEquities);
            List<MarketEquityResponse.MarketEquityResponseElement> equities = new ArrayList<>();
            while (!futureEquitiesTask.isDone()) {
                equities = futureEquitiesTask.get();

            }
            if (equities.isEmpty()) {
                throw new IllegalArgumentException(ExceptionMessage.MARKET_EQUITY_INFORMATION_NOT_AVAILABLE.msg);
            }


            CompletableFuture<List<MarketCedearResponse>> futureCedearsTask = CompletableFuture.supplyAsync(this::getTotalCedears);
            List<MarketCedearResponse> cedears = new ArrayList<>();
            while (!futureCedearsTask.isDone()) {
                cedears = futureCedearsTask.get();

            }
            if (cedears.isEmpty()) {
                throw new IllegalArgumentException(ExceptionMessage.MARKET_EQUITY_INFORMATION_NOT_AVAILABLE.msg);
            }

            marketResponses.addAll(bonds);
            marketResponses.addAll(equities);
            marketResponses.addAll(cedears);
            return marketResponses;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    default List<MarketResponse> getMarketResponses() {
        return marketResponses.isEmpty() ? updateCurrentMarketPrices() : marketResponses;
    }
}
