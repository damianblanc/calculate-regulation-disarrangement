package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Tests all CRUD operations over {@link FCIRegulation}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CalculateRegulationDisarrangementApplication.class)
@AutoConfigureMockMvc(addFilters = false)

@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class FCICRUDTestSuite {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createFCIRegulationTest() throws Exception {
        /* Create Specie Type Group and Specie Types to include in FCI Regulation */
        String stgContent = objectMapper.writeValueAsString(createFCISpecieTypeGroup());
        mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/component/specie-type-group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stgContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String content = objectMapper.writeValueAsString(createFCIRegulationBean());
        mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFCIRegulationCreationResponseContent()))
                .andDo(MockMvcResultHandlers.print());

        String content2 = objectMapper.writeValueAsString(createFCIRegulationBean());
        mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFCIRegulationCreationResponseContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteFCIRegulationTest() throws Exception {
        String content = objectMapper.writeValueAsString(createFCIRegulationBean());
        String createContent = mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        String deleteContent = mockmvc.perform(MockMvcRequestBuilders.delete("/api/v1/fci/ALFA"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(getFCIRegulationCreationResponseContent()))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateFCIRegulationTest() throws Exception {
        String contentToCreate = objectMapper.writeValueAsString(createFCIRegulationBean());
        String creationContent = mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentToCreate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        FCIRegulation fciRegulation = objectMapper.readValue(creationContent, FCIRegulation.class);
        fciRegulation.setName("Alpha Mix Rent FCI ARG");
//        fciRegulation.getComposition().removeIf(e -> e.getFciSpecieTypeId().equals(SpecieTypeGroupEnum.Bond.name()));
        String contentToUpdate = objectMapper.writeValueAsString(fciRegulation);

        String updatingContent = mockmvc.perform(MockMvcRequestBuilders.put("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(contentToUpdate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createSpecieTypeGroupTest() throws Exception {
        String content = objectMapper.writeValueAsString(createFCISpecieTypeGroup());
        mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/component/specie-type-group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(getFCIRegulationCreationResponseContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findFCIRegulationNotFound() {
        Throwable exception = assertThrows(jakarta.servlet.ServletException.class, () ->
                mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/fci/ALFAS")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn());
        assertEquals(exception.getCause().getClass(), jakarta.persistence.EntityNotFoundException.class);
        assertEquals(exception.getCause().getMessage(), String.format(ExceptionMessage.FCI_REGULATION_ENTITY_NOT_FOUND.msg, "ALFAS"));
    }

    @Test
    public void findFCIRegulation() throws Exception {
        String content = objectMapper.writeValueAsString(createFCIRegulationBean());
        String creationContentAsString = mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/fci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(getFCIRegulationCreationResponseContent()))
                .andReturn().getResponse().getContentAsString();
        FCIRegulation fciRegulation = objectMapper.readValue(creationContentAsString, FCIRegulation.class);

        String findingContentAsString = mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/fci/ALFA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
//        assertEquals(creationContentAsString, findingContentAsString);
    }

    @Test
    public void listFCIRegulations() throws Exception {
        ResultActions resultActions = mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/fci")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * {"id":null,"name":"Equity","description":"Wraps all Equity specie types","fciSpecieTypes":
     * [{"fciSpecieTypeId":null,"name":"Agricultures","description":"Comprehends all agriculture companies type equities"},
     * {"fciSpecieTypeId":null,"name":"Energies","description":"Comprehends all energy companies type equities"},
     * {"fciSpecieTypeId":null,"name":"Banks","description":"Comprehends all bank type equities"}]}
     */
    private FCISpecieTypeGroup createFCISpecieTypeGroup() {
        FCISpecieType banks = FCISpecieType.builder().name("Banks").description("Comprehends all bank type equities").build();
        FCISpecieType energies = FCISpecieType.builder().name("Energies").description("Comprehends all energy companies type equities").build();
        FCISpecieType agricultures = FCISpecieType.builder().name("Agricultures").description("Comprehends all agriculture companies type equities").build();
        return FCISpecieTypeGroup.builder().name("Equity").description("Wraps all Equity specie types")
                .fciSpecieTypes(Set.of(banks, energies, agricultures)).build();
    }

    /**
     * {"id":1,"symbol":"ALFA","name":"Alpha Mix Rent FCI","description":"Comprehends a mix of Equities, Bunds and Cash to allow retrievals","composition":
     * [{"fciCompositionId":null,"fciSpecieType":{"fciSpecieTypeId":2,"name":"Energies","description":"Comprehends all energy companies type equities"},"percentage":50.0},
     * {"fciCompositionId":null,"fciSpecieType":{"fciSpecieTypeId":1,"name":"Banks","description":"Comprehends all bank type equities"},"percentage":30.0},
     * {"fciCompositionId":null,"fciSpecieType":{"fciSpecieTypeId":3,"name":"Agricultures","description":"Comprehends all agriculture companies type equities"},"percentage":20.0}]}
     */
    private FCIRegulation createFCIRegulationBean() {
        FCISpecieType banks = FCISpecieType.builder().fciSpecieTypeId(1).name("Banks").description("Comprehends all bank type equities").build();
        FCISpecieType energies = FCISpecieType.builder().fciSpecieTypeId(2).name("Energies").description("Comprehends all energy companies type equities").build();
        FCISpecieType agricultures = FCISpecieType.builder().fciSpecieTypeId(3).name("Agricultures").description("Comprehends all agriculture companies type equities").build();
        FCIRegulation fciRegulation = FCIRegulation.builder().id(1).name("Alpha Mix Rent FCI").symbol("ALFA").description("Comprehends a mix of Equities, Bunds and Cash to allow retrievals").build();
        FCIComposition fciCompositionBond = FCIComposition.builder().percentage(30.0).fciSpecieTypeId(banks.getFciSpecieTypeId()).build();
        FCIComposition fciCompositionShareMarket = FCIComposition.builder().percentage(50.0).fciSpecieTypeId(energies.getFciSpecieTypeId()).build();
        FCIComposition fciCompositionCash = FCIComposition.builder().percentage(20.0).fciSpecieTypeId(agricultures.getFciSpecieTypeId()).build();
        Set<FCIComposition> fciCompositionList = Set.of(fciCompositionShareMarket, fciCompositionBond, fciCompositionCash);
        fciRegulation.setComposition(fciCompositionList);
        return fciRegulation;
    }

    private String getFCIRegulationCreationResponseContent() {
        return "{\"id\":1,\"symbol\":\"ALFA\",\"name\":\"Alpha Mix Rent FCI\",\"description\":\"Comprehends a mix of Equities, Bunds and Cash to allow retrievals\",\"composition\":[{\"fciCompositionId\":3,\"fciSpecieType\":{\"fciSpecieTypeId\":1,\"name\":\"Banks\",\"description\":\"Comprehends all bank type equities\"},\"percentage\":30.0},{\"fciCompositionId\":1,\"fciSpecieType\":{\"fciSpecieTypeId\":3,\"name\":\"Agricultures\",\"description\":\"Comprehends all agriculture companies type equities\"},\"percentage\":20.0},{\"fciCompositionId\":2,\"fciSpecieType\":{\"fciSpecieTypeId\":2,\"name\":\"Energies\",\"description\":\"Comprehends all energy companies type equities\"},\"percentage\":50.0}]}";
//        return "{\"id\":1,\"name\":\"Alpha Mix Rent FCI\",\"symbol\":\"ALFA\",\"composition\":[{\"id\":2,\"specieType\":\"BOND\",\"percentage\":30.0},{\"id\":3,\"specieType\":\"CASH\",\"percentage\":20.0},{\"id\":1,\"specieType\":\"MARKET_SHARE\",\"percentage\":50.0}],\"fciregulationComposition\":{\"MARKET_SHARE\":50.0,\"BOND\":30.0,\"CASH\":20.0},\"compositionAsSpecieType\":{\"MARKET_SHARE\":50.0,\"CASH\":20.0,\"BOND\":30.0}}";
    }
}
