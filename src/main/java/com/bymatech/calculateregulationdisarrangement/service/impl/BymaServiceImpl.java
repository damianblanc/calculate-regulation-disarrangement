package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.BymaHttpService;
import com.bymatech.calculateregulationdisarrangement.service.BymaService;
import com.bymatech.calculateregulationdisarrangement.service.BymaAPIServiceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class BymaServiceImpl implements BymaService {

    public List<BymaCedearResponse> getCedears(@Body BymaCedearAuthBean bymaCedearAuthBean) {
        BymaHttpService service = BymaAPIServiceGenerator.createService(BymaHttpService.class);
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
    public BymaBondResponse getBonds(@Body BymaBondAuthBean bymaBondAuthBean) {
        BymaHttpService service = BymaAPIServiceGenerator.createService(BymaHttpService.class);
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
    public List<BymaBondResponse.BymaBondResponseElement> getBondsOrderByPrice(OrderType orderType) {
        List<BymaBondResponse.BymaBondResponseElement> bonds = getBonds(BymaBondAuthBean.create()).getBymaBondResponses().stream().sorted(Comparator.comparing(BymaBondResponse.BymaBondResponseElement::getPrice)).toList();
        return orderType == OrderType.DESC ? bonds : bonds.stream().sorted(Collections.reverseOrder()).toList();
    }

    @Override
    public List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType) {
        return getBondsOrderByPrice(orderType).stream()
                .map(bond -> new SpecieCurrentPriceVO(bond.getDescription(), Double.valueOf(bond.getPrice())))
                .toList();
    }

}
