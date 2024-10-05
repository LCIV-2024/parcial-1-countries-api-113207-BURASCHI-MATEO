package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestPost;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("")
    public ResponseEntity<List<CountryDTO>> getAllCountries(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String code) {
        return ResponseEntity.ok(countryService.getAllCountries(name, code));
    }

    @GetMapping("{continent}/continent")
    public ResponseEntity<List<CountryDTO>> getCountriesByContinent(@PathVariable String continent) {
        return ResponseEntity.ok(countryService.getCountriesByContinent(continent));
    }

    @GetMapping("{language}/language")
    public ResponseEntity<List<CountryDTO>> getCountriesByLanguage(@PathVariable String language) {
        return ResponseEntity.ok(countryService.getCountriesByLanguage(language));
    }

    @GetMapping("/most-borders")
    public ResponseEntity<CountryDTO> getCountryWithMostBorders() {
        return ResponseEntity.ok(countryService.getCountryWithMostBorders());
    }

    @PostMapping("/")
    public ResponseEntity<?> saveCountries(@RequestBody RequestPost request) {
        if (request.getAmountOfCountryToSave() > 10) {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(countryService.saveCountries(request));
    }

}