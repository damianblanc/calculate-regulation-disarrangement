package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.http.Body;

import java.util.ArrayList;
import java.util.List;

@Service
public interface MarketHttpService {

    List<MarketResponse> marketResponses = new ArrayList<>();

    List<MarketCedearResponse> getCedears(@Body MarketCedearAuthBean marketCedearAuthBean);

    List<MarketBondResponse.MarketBondResponseElement> getTotalBonds();

    MarketBondResponse getBonds(@Body MarketBondAuthBean marketBondAuthBean);

    List<MarketBondResponse.MarketBondResponseElement> getBondsOrderedByPrice(OrderType orderType);

    List<MarketResponse> getBondsOrderByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition);

    List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType);

    MarketEquityResponse getEquities(@Body MarketEquityAuthBean marketEquityAuthBean);

    List<MarketEquityResponse.MarketEquityResponseElement> getTotalEquities();

    List<MarketResponse> getEquityOrderedByPrice(OrderType orderType);

    List<MarketResponse> getEquityOrderedByPriceFilteredBySpecieList(OrderType orderType, List<FCISpeciePosition> speciesPosition);

    /**
     * An Asynchronous fixed delay scheduled task to retrieve current market prices for all Specie Type Groups
     */
//    @Scheduled(fixedDelayString = "${schedule.advice.position.fixed.delay.minutes:5}")
//    @Scheduled(fixedDelayString = "${schedule.advice.position.fixed.delay.seconds:2}000")
    default void updateCurrentMarketPrices() {
        //TODO: See that comunication can fail, then do not clear before getting results, test comunicacion
        //TODO: Does returned data does not change after stock round closure? I/E: 17.00, until 11.00 next day?
        //TODO: Avoid going to retrieve data when it becomes constant.
        marketResponses.clear();
        marketResponses.addAll(getTotalBonds());
        marketResponses.addAll(getTotalEquities());
    }

    default List<MarketResponse> getMarketResponses() {
        return marketResponses;
    }
}
