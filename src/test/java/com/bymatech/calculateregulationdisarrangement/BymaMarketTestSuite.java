package com.bymatech.calculateregulationdisarrangement;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CalculateRegulationDisarrangementApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class BymaMarketTestSuite {

    @Autowired
    private MockMvc mockmvc;

    @Test
    public void getCedearsTest() throws Exception {
        mockmvc.perform(
                MockMvcRequestBuilders.get("/api/v1/cedears")).andReturn();
    }

    @Test
    public void getBondsTest() throws Exception {
        mockmvc.perform(
                MockMvcRequestBuilders.get("/api/v1/bonds")).andReturn();
    }

    @Test
    public void getBondPricesTest() throws Exception {
        MockHttpServletResponse response = mockmvc.perform(
                MockMvcRequestBuilders.get("/api/v1/bonds/prices")).andReturn().getResponse();
    }

}
