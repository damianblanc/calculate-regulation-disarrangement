package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import org.springframework.stereotype.Service;
import retrofit2.http.Body;

import java.util.List;

@Service
public interface BymaService {

    List<BymaCedearResponse> getCedears(@Body BymaCedearAuthBean bymaCedearAuthBean);

    BymaBondResponse getBonds(@Body BymaBondAuthBean bymaBondAuthBean);

    List<BymaBondResponse.BymaBondResponseElement> getBondsOrderByPrice(OrderType orderType);

    List<SpecieCurrentPriceVO> getBondsOrderByPriceVO(OrderType orderType);
}
