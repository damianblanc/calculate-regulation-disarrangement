package com.bymatech.calculateregulationdisarrangement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CalculateRegulationDisarrangementApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class FCIAdvisorTestSuite extends FCITestFixture {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Calculates advising by price criteria
     * When position for specie type is disarranged from FCI regulated defined percentage it could by over or bellow, defining
     * to sell in fist scenario or buy in the second one.
     * Price criteria indicates to do so by taking highest specie prices on selling and lowest prices on buying.
     * There must be a slice taken from specie type list, in example, the fists five ones.
     * Also there should be a spread definition, line uniform distribution, in this case assigning equal selling or buying priority to
     * each of taken species.
     *
     * @throws Exception Indicating criteria is not accepted, then any internal server error on process
     */
    @Test
    public void advicePositionByPriceTest() throws Exception {
        fciRegulation1 = createFCIRegulation1();
        speciePositionList1 = createSpeciePositionList1();
        fciPosition1 = createFCIPosition(fciRegulation1, speciePositionList1);

        String content = objectMapper.writeValueAsString(fciPosition1);
        mockmvc.perform(
                MockMvcRequestBuilders.post("/api/v1/calculate-disarrangement/advice/criteria/price_uniformly_distribution_limit_5_elements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getExpectedContent()))
                .andDo(MockMvcResultHandlers.print());

    }

    private String getExpectedContent() {
        return "{\"BOND\":[{\"specieName\":\"BONO EXT. GLOBAL U$S STEP UP VTO. 9/7/2041\",\"operationAdvice\":\"SELL\",\"quantity\":1},{\"specieName\":\"BONO DEL TESORO NAC. AJ. CER 3.75% VTO. 14/04/24\",\"operationAdvice\":\"SELL\",\"quantity\":83}]}";
    }
}

