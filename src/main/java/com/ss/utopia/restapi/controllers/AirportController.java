package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirportRepository;
import com.ss.utopia.restapi.models.Airport;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<Airport> getAirport(@PathVariable String iata) throws ResponseStatusException {
        return new ResponseEntity<Airport>(airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Airport not found!")
            ),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<Airport>> getAllAirports() {
        return new ResponseEntity<Iterable<Airport>>(
            airportDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirport(@RequestBody Airport airport) {
        try {
            return new ResponseEntity<>(
                airportDB.save(airport),
                HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    @PutMapping(path="/{iata}")
    public ResponseEntity<?> updateAirport(@PathVariable String iata, @RequestBody Airport airportDetails) throws ResponseStatusException {
        Airport airport = airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Airport not found!"
            )
        );

        airport.setCityName(airportDetails.getCityName());

        try {
            Airport updatedAirport = airportDB.save(airport);
            return new ResponseEntity<>(updatedAirport, HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    @DeleteMapping("/{iata}")
    public ResponseEntity<?> deleteAirport(@PathVariable String iata) throws ResponseStatusException {
        Airport airport = airportDB
            .findById(iata)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Airport could not be found!"
                )
            );

        try {
            airportDB.delete(airport);
            return new ResponseEntity<>(airport, HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }
}
