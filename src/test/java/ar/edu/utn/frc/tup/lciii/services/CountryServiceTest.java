package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;


    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("01/ get all coutries ok")
    public void getAllCountriesOk() {
        Map<String, Object> objectResponse = new HashMap<>();
        objectResponse.put("name", "Argentina");
        objectResponse.put("cca3", "ARG");

        List<Map<String, Object>> apiResponse = List.of(objectResponse);

        List<CountryDTO> countries = List.of(new CountryDTO("ARG", "Argentina"));
        String url = "https://restcountries.com/v3.1/all";

        when(restTemplate.getForObject(url, List.class)).thenReturn(apiResponse);

        List<CountryDTO> response = countryService.getAllCountries(null, null);

        assertEquals(response, countries);
    }
}
