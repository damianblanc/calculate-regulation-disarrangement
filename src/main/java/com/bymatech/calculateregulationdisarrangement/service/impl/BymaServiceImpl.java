package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.BymaHttpService;
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
public class BymaServiceImpl implements BymaHttpService {

    public List<BymaCedearResponse> getCedears(@Body BymaCedearAuthBean bymaCedearAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service = BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<List<BymaCedearResponse>> callSync = service.getCedears(bymaCedearAuthBean);

        try {
            Response<List<BymaCedearResponse>> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return List.of();
    }

    @Override
    public List<BymaBondResponse.BymaBondResponseElement> getTotalBonds() {
        BymaBondResponse marketBonds = getBonds(BymaBondAuthBean.create(1));
        List<BymaBondResponse.BymaBondResponseElement> bonds = new ArrayList<>(marketBonds.getBymaBondResponses());

        for (int i = 2; i <= marketBonds.getContent().getPageCount(); i++) {
            List<BymaBondResponse.BymaBondResponseElement> bymaBondResponses = getBonds(BymaBondAuthBean.create(i)).getBymaBondResponses();
            bonds.addAll(bymaBondResponses);
        }

        return bonds;
    }

    @Override
    public BymaBondResponse getBonds(@Body BymaBondAuthBean bymaBondAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service = BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<BymaBondResponse> callSync = service.getBonds(bymaBondAuthBean);

        try {
            Response<BymaBondResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return BymaBondResponse.create();
    }

    @Override
    public List<BymaBondResponse.BymaBondResponseElement> getBondsOrderedByPrice(OrderType orderType) {
        List<BymaBondResponse.BymaBondResponseElement> bonds =
                getTotalBonds().stream().sorted(Comparator.comparing(BymaBondResponse.BymaBondResponseElement::getPrice)).toList();
        return orderType == OrderType.DESC ? bonds : bonds.stream().sorted(Collections.reverseOrder()).toList();
    }

    @Override
    public List<BymaBondResponse.BymaBondResponseElement> getBondsOrderByPriceFilteredBySpecieList(OrderType orderType, List<SpeciePosition> speciesPosition) {
        return getBondsOrderedByPrice(orderType).stream()
                .filter(bond -> speciesPosition.stream()
                        .anyMatch(specie -> specie.getSymbol().equals(bond.getSymbol())))
                .toList();
    }

    @Override
    public List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType) {
        return getBondsOrderedByPrice(orderType).stream()
                .map(bond -> new SpecieCurrentPriceVO(bond.getDescription(), Double.valueOf(bond.getPrice())))
                .toList();
    }

    @Override
    public BymaEquityResponse getEquities(BymaEquityAuthBean bymaEquityAuthBean) {
        com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService service =
                BymaAPIServiceGenerator.createService(com.bymatech.calculateregulationdisarrangement.service.http.BymaHttpService.class);
        Call<BymaEquityResponse> callSync = service.getEquities(bymaEquityAuthBean);

        try {
            Response<BymaEquityResponse> response = callSync.execute();
            return response.body();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return BymaEquityResponse.create();
    }

    @Override
    public List<BymaEquityResponse.BymaEquityResponseElement> getEquityOrderByPriceFilteredBySpecieList(OrderType orderType, List<SpeciePosition> speciesPosition) {
        return getEquityOrderedByPrice(orderType).stream()
                .filter(equity -> speciesPosition.stream()
                        .anyMatch(specie -> specie.getSymbol().equals(equity.getSymbol())))
                .toList();
    }

    @Override
    public List<BymaEquityResponse.BymaEquityResponseElement> getEquityOrderedByPrice(OrderType orderType) {
        BymaEquityResponse marketEquities = getEquities(BymaEquityAuthBean.create());
        List<BymaEquityResponse.BymaEquityResponseElement> equities =
                marketEquities.getBymaEquityResponses().stream().sorted(Comparator.comparing(
                        BymaEquityResponse.BymaEquityResponseElement::getTrade)).toList();
        return orderType == OrderType.DESC ? equities : equities.stream().sorted(Collections.reverseOrder()).toList();
    }
}
