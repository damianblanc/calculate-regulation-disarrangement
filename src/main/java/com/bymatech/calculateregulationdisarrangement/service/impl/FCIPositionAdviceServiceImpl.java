package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.repository.FCIPositionAdviceRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class FCIPositionAdviceServiceImpl implements FCIPositionAdviceService {

    @Autowired
    private FCIPositionAdviceRepository fciPositionAdviceRepository;

    @Override
    public FCIPositionAdvice registerAdvice(String advice, String owner) {
        return fciPositionAdviceRepository.save(FCIPositionAdvice.builder()
                .owner(owner)
                .timestamp(Timestamp.from(Instant.now()))
                .advice(advice).build());
    }

    @Override
    public List<String> getAllAdvices() {
        return fciPositionAdviceRepository.findAll().stream().map(FCIPositionAdvice::getAdvice).toList();
    }

    @Override
    public List<FCIPositionAdvice> listAllAdvices() {
        return fciPositionAdviceRepository.findAll();
    }


}
