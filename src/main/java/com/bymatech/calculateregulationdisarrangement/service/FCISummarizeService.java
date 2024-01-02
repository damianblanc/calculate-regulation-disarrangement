package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import org.springframework.stereotype.Service;

@Service
public interface FCISummarizeService {

    SummarizeOverviewVO retrieveSummarizeOverview() throws Exception;
}
