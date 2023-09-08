package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.controller.FCICalculationController;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CalculateRegulationDisarrangementApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class FCIRegulationTestSuite extends FCITestFixture {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private FCICalculationController userController;

    @Autowired
    ObjectMapper objectMapper;


    @Test()
    public void forceFciRegulationPercentageCompositionFailTest() throws Exception {
        ImmutableMap<SpecieType, Double> fCIRegulationWrongComposition = ImmutableMap.<SpecieType, Double>builder()
                .put(SpecieType.MARKET_SHARE, 30.00)
                .put(SpecieType.BOND, 50.00)
                .put(SpecieType.CASH, 10.00).build();

        FCIRegulation fciRegulation = createFCIRegulation(fCIRegulationName1, fCIRegulationWrongComposition);
        FCIPosition fciPosition = createFCIPosition(fciRegulation, createSpeciePositionList1());

        String content = objectMapper.writeValueAsString(fciPosition);
        Throwable exception = assertThrows(jakarta.servlet.ServletException.class, () -> postCalculateDisarrangement(content));
        assertEquals(exception.getCause().getClass(), FailedValidationException.class);
        assertEquals(exception.getCause().getMessage(), ExceptionMessage.TOTAL_PERCENTAGE.msg);
    }

    /**
     * Tests that FCI indicated position specie types does not match all expected FCI regulation Specie types
     */
    @Test()
    public void forceFciRegulationSpecieTypeCompositionFailTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        FCIPosition fciPosition = createFCIPosition(fciRegulation1, createSpeciePositionList2());

        String content = objectMapper.writeValueAsString(fciPosition);
        Throwable exception = assertThrows(jakarta.servlet.ServletException.class, () -> postCalculateDisarrangement(content));
        assertEquals(exception.getCause().getClass(), IllegalArgumentException.class);
        assertEquals(exception.getCause().getMessage(), String.format(ExceptionMessage.REGULATION_SPECIE_TYPE_DOES_NOT_MATCH.msg, SpecieType.CASH));
    }

    private void postCalculateDisarrangement(String content) throws Exception {
        mockmvc.perform(
                MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
    }

    /**
     * Performs disarrangement calculation given a scenario with FCI regulation percentages and current FCI position
     */
    @Test
    public void calculateDisarrangementTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        speciePositionList1 = createSpeciePositionList1();
        fciPosition1 = createFCIPosition(fciRegulation1, speciePositionList1);
        String expectedContent =
        "{\"regulationLags\":{\"MARKET_SHARE\":7.979212813085702,\"BOND\":-5.017890611688529,\"CASH\":-2.9613222013971736},\n" +
        "         \"valuedLags\":{\"MARKET_SHARE\":11707.499999999998,\"BOND\":-7362.4999999999945,\"CASH\":-4345.000000000003},\n" +
        "         \"percentagePosition\":{\"MARKET_SHARE\":37.9792128130857,\"BOND\":44.98210938831147,\"CASH\":17.038677798602826},\n" +
        "         \"valuedPosition\":{\"MARKET_SHARE\":55725.0,\"BOND\":66000.0,\"CASH\":25000.0}}";

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedContent))
                .andDo(MockMvcResultHandlers.print());
    }

}
