package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirportRepository;
import com.ss.utopia.restapi.models.Airport;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/airports")
public class AirportController {

    @Autowired
    AirportRepository airportDB;

    @GetMapping(path="/{iata}")
    public Airport getAirport(@PathVariable String iata) throws ResponseStatusException {
        return airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airport not found!"));
    }

    @GetMapping(path="/all")
    public Iterable<Airport> getAllAirports() {
        return airportDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirport(@RequestBody Airport airport) {
        return new ResponseEntity<>(airportDB.save(airport), HttpStatus.OK);
    }

    @PutMapping(path="/{iata}")
    public ResponseEntity<?> updateAirport(@PathVariable String iata, @RequestBody Airport airportDetails) throws ResponseStatusException {
        Airport airport = airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airport not found!")
        );

        airport.setCityName(airportDetails.getCityName());
        airport.setIataID(airportDetails.getIataID());

        Airport updatedAirport = airportDB.save(airport);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }

    @DeleteMapping("/{iata}")
    public ResponseEntity<?> deleteAirport(@PathVariable String iata) throws ResponseStatusException {
        Airport airport = airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airport could not be found!"));

        airportDB.delete(airport);
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }
}
