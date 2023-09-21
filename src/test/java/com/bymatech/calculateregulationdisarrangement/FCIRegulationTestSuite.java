package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
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


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CalculateRegulationDisarrangementApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class FCIRegulationTestSuite extends FCITestFixture {

    @Autowired
    private MockMvc mockmvc;

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

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getExpectedContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void calculateDisarrangementVerboseTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        speciePositionList1 = createSpeciePositionList1();
        fciPosition1 = createFCIPosition(fciRegulation1, speciePositionList1);

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement/verbose")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getExceptedVerboseContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void calculateDisarrangementPercentagesTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        speciePositionList1 = createSpeciePositionList1();
        fciPosition1 = createFCIPosition(fciRegulation1, speciePositionList1);

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement/percentages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"CASH\":-2.9613222013971736,\"BOND\":-5.017890611688529,\"MARKET_SHARE\":7.979212813085702}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void calculateDisarrangementValuedTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        speciePositionList1 = createSpeciePositionList1();
        fciPosition1 = createFCIPosition(fciRegulation1, speciePositionList1);

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement/valued")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"CASH\":-4345.000000000003,\"BOND\":-7362.4999999999945,\"MARKET_SHARE\":11707.499999999998}"))
                .andDo(MockMvcResultHandlers.print());
    }


    private static String getExpectedContent() {
        return "{\"regulationLags\":{\"MARKET_SHARE\":7.979212813085702,\"BOND\":-5.017890611688529,\"CASH\":-2.9613222013971736},\n" +
        "         \"valuedLags\":{\"MARKET_SHARE\":11707.499999999998,\"BOND\":-7362.4999999999945,\"CASH\":-4345.000000000003},\n" +
        "         \"percentagePosition\":{\"MARKET_SHARE\":37.9792128130857,\"BOND\":44.98210938831147,\"CASH\":17.038677798602826},\n" +
        "         \"valuedPosition\":{\"MARKET_SHARE\":55725.0,\"BOND\":66000.0,\"CASH\":25000.0}}";
    }

    private static String getExceptedVerboseContent() {
       return "{\n" +
                "  \"regulationLagOutcome\": {\n" +
                "    \"regulationLags\": {\n" +
                "      \"CASH\": -2.9613222013971736,\n" +
                "      \"BOND\": -5.017890611688529,\n" +
                "      \"MARKET_SHARE\": 7.979212813085702\n" +
                "    },\n" +
                "    \"valuedLags\": {\n" +
                "      \"CASH\": -4345.000000000003,\n" +
                "      \"BOND\": -7362.4999999999945,\n" +
                "      \"MARKET_SHARE\": 11707.499999999998\n" +
                "    },\n" +
                "    \"percentagePosition\": {\n" +
                "      \"CASH\": 17.038677798602826,\n" +
                "      \"BOND\": 44.98210938831147,\n" +
                "      \"MARKET_SHARE\": 37.9792128130857\n" +
                "    },\n" +
                "    \"valuedPosition\": {\n" +
                "      \"CASH\": 25000.0,\n" +
                "      \"BOND\": 66000.0,\n" +
                "      \"MARKET_SHARE\": 55725.0\n" +
                "    }\n" +
                "  },\n" +
                "  \"fciPosition\": {\n" +
                "    \"fciRegulation\": {\n" +
                "      \"name\": \"Alpha Mix Rent FCI\",\n" +
                "      \"composition\": {\n" +
                "        \"MARKET_SHARE\": 30.0,\n" +
                "        \"BOND\": 50.0,\n" +
                "        \"CASH\": 20.0\n" +
                "      }\n" +
                "    },\n" +
                "    \"fciPositionList\": [\n" +
                "      {\n" +
                "        \"name\": \"GGAL\",\n" +
                "        \"specieType\": \"MARKET_SHARE\",\n" +
                "        \"price\": 3.15,\n" +
                "        \"quantity\": 1500\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"YPF\",\n" +
                "        \"specieType\": \"MARKET_SHARE\",\n" +
                "        \"price\": 8.5,\n" +
                "        \"quantity\": 6000\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"GD30\",\n" +
                "        \"specieType\": \"BOND\",\n" +
                "        \"price\": 0.6,\n" +
                "        \"quantity\": 40000\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"AL30\",\n" +
                "        \"specieType\": \"BOND\",\n" +
                "        \"price\": 1.4,\n" +
                "        \"quantity\": 30000\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"CASH\",\n" +
                "        \"specieType\": \"CASH\",\n" +
                "        \"price\": 25000.0,\n" +
                "        \"quantity\": 1\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

}
