package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface FCIPositionAdviceService {

    /**
     * Registers an Advice
     * Note: owner is to be changed to user when implementing security
     * @param advice advice in json format as exposed in http
     * @param owner process executor requester
     * @return Registered advice
     */
    FCIPositionAdvice registerAdvice(String advice, String owner);

    List<String> getAllAdvices();

    List<FCIPositionAdvice> listAllAdvices();

}
