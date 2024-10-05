package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestPost;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private CountryRepository countryRepository;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private ModelMapper modelMapper;

        public List<CountryDTO> getAllCountries(String name, String code) {
                String url = "https://restcountries.com/v3.1/all";
                if (name != null) {
                        url = "https://restcountries.com/v3.1/name/" + name;
                } else if (code != null) {
                        url = "https://restcountries.com/v3.1/alpha/" + code.toUpperCase();
                }
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList())
                        .stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByContinent(String continent) {
                String url = "https://restcountries.com/v3.1/region/" + continent;
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList())
                        .stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByLanguage(String language) {
                String url = "https://restcountries.com/v3.1/lang/" + language;
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList())
                        .stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        public CountryDTO getCountryWithMostBorders() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                List<Country> countries = response.stream().map(this::mapToCountry).toList();
                CountryDTO countryDTO = countries.stream()
                        .filter(country -> country.getBorders() != null)
                        .sorted(Comparator.comparingInt(country -> country.getBorders().size()))
                        .map(this::mapToDTO).findFirst().get();
                return countryDTO;
        }

        public List<CountryDTO> saveCountries(RequestPost request) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                List<Country> countries = response.stream().map(this::mapToCountry).toList();
                List<Country> countriesToSave = countries.subList(0, request.getAmountOfCountryToSave());
                List<CountryEntity> countriesEntities = countriesToSave.stream()
                        .map(country -> modelMapper.map(country, CountryEntity.class)).collect(Collectors.toList());
                List<CountryEntity> countriesEntitiesSaved = countryRepository.saveAll(countriesEntities);
                List<CountryDTO> countriesDTO = countriesEntitiesSaved.stream()
                        .map(entity -> modelMapper.map(entity, CountryDTO.class)).collect(Collectors.toList());
                return countriesDTO;
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                List<String> borders = (countryData.get("borders") != null)
                        ? (List<String>) nameData.get("borders")
                        : List.of();
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .code((String) countryData.get("cca3"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .borders(borders)
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }
}