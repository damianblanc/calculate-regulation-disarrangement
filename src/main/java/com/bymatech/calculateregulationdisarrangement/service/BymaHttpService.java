package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import org.springframework.stereotype.Service;
import retrofit2.http.Body;

import java.util.List;

@Service
public interface BymaHttpService {

    List<BymaCedearResponse> getCedears(@Body BymaCedearAuthBean bymaCedearAuthBean);

    List<BymaBondResponse.BymaBondResponseElement> getTotalBonds();

    BymaBondResponse getBonds(@Body BymaBondAuthBean bymaBondAuthBean);

    List<BymaBondResponse.BymaBondResponseElement> getBondsOrderedByPrice(OrderType orderType);

    List<BymaBondResponse.BymaBondResponseElement> getBondsOrderByPriceFilteredBySpecieList(OrderType orderType, List<SpeciePosition> speciesPosition);

    List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType);

    BymaEquityResponse getEquities(@Body BymaEquityAuthBean bymaEquityAuthBean);

    List<BymaEquityResponse.BymaEquityResponseElement> getEquityOrderedByPrice(OrderType orderType);

    List<BymaEquityResponse.BymaEquityResponseElement> getEquityOrderByPriceFilteredBySpecieList(OrderType orderType, List<SpeciePosition> speciesPosition);
}
