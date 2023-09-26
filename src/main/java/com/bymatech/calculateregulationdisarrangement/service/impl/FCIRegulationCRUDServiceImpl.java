package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPositionAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIPositionAdviceRepository;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Comprehends CRUD operations over {@link FCIRegulation}
 */
@Service
public class FCIRegulationCRUDServiceImpl  implements FCIRegulationCRUDService {

    @Autowired
    private FCIRegulationRepository fciRegulationRepository;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Override
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public FCIRegulation createFCIRegulation(FCIRegulation fciRegulation) {
//        EntityManagerFactory emf= Persistence.createEntityManagerFactory("fciRegulationFactory");
//        EntityManager em = emf.createEntityManager();
//        FCIRegulation fciRegulationA = FCIRegulation.builder().name("Alpha Mix Rent FCI").symbol("ALFA").build();
//        em.getTransaction().begin();
//        em.persist(fciRegulationA);
//        em.flush();
//        em.refresh(fciRegulationA);
//
////        fciRegulationRepository.save(fciRegulationA);
//        FCIComposition fciCompositionBond = FCIComposition.builder().percentage(30.0).specieType(SpecieType.BOND.name()).build();
////        FCIComposition fciCompositionShareMarket = FCIComposition.builder().fciRegulation(fciRegulationA).percentage(50.0).specieType(SpecieType.MARKET_SHARE.name()).build();
////        FCIComposition fciCompositionCash = FCIComposition.builder().fciRegulation(fciRegulationA).percentage(20.0).specieType(SpecieType.CASH.name()).build();
////        Set<FCIComposition> fciCompositionList = Set.of(fciCompositionShareMarket, fciCompositionBond, fciCompositionCash);
//        Set<FCIComposition> composition = fciRegulationA.getComposition();
//        composition.add(fciCompositionBond);
//
////        Set.of(fciCompositionBond));
////        fciRegulationA.setComposition(Set.of(fciCompositionBond));
//        em.persist(fciRegulationA);
////        fciRegulationA.setComposition(fciCompositionList);
        return fciRegulationRepository.save(fciRegulation);
    }

    @Override
    public FCIRegulation deleteFCIRegulation(String symbol) throws EntityNotFoundException {
        FCIRegulation toDelete = fciRegulationRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
        fciRegulationRepository.delete(toDelete);
        return toDelete;
    }

    @Override
    public FCIRegulation updateFCIRegulation(FCIRegulation fciRegulation) {
        fciRegulationRepository.findBySymbol(fciRegulation.getSymbol())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, fciRegulation.getSymbol())));
        return fciRegulationRepository.save(fciRegulation);
    }

    @Override
    public FCIRegulation findFCIRegulation(String symbol) {
        return fciRegulationRepository.findBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, symbol)));
    }

    @Override
    public Optional<FCIRegulation> findFCIRegulationOptional(String symbol) {
        return fciRegulationRepository.findBySymbol(symbol);
    }

    @Override
    public FCIRegulation findOrCreateFCIRegulation(@NotNull FCIRegulationDTO fciRegulationDTO) {
        return fciRegulationRepository.findBySymbol(fciRegulationDTO.getSymbol())
            .orElseGet(() -> fciRegulationRepository.save(FCIRegulation.builder()
                    .name(fciRegulationDTO.getName())
                    .symbol(fciRegulationDTO.getSymbol())
                    .composition(fciRegulationDTO.getComposition())
                    .build()));
    }

    @Override
    public Set<FCIRegulation> listFCIRegulations() {
        return fciRegulationRepository.findAll().stream()
                .peek(fciRegulation -> fciRegulation.setFCIPositionAdvices(fciPositionAdviceService.listAllAdvices()))
                .collect(Collectors.toSet());
    }
}
