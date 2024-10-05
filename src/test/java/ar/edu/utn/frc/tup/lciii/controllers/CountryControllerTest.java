package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestPost;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CountryService countryService;

    @Test
    @DisplayName("01 get all countries ok")
    void getAllCountriesOk() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("02 get continent")
    void getAllCountriesByContinentOk() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/countries/{continent}/continent", "europe"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("03 get language")
    void getAllCountriesBylanguageOk() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/countries/{language}/language", "english"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("04 get most borders")
    void getMostBordersOk() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/countries/most-borders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("05 save countries ok")
    void saveCountriesOk() throws Exception {
        RequestPost requestPost = new RequestPost();
        requestPost.setAmountOfCountryToSave(10);

        given(countryService.saveCountries(requestPost)).willReturn(List.of(new CountryDTO()));

        mvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amountOfCountryToSave\": 10}"))
                .andExpect(status().isOk());
    }

}
