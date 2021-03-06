package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.FlightRepository;
import com.ss.utopia.restapi.models.Flight;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/flights")
public class FlightController {

    @Autowired
    FlightRepository flightDB;

    @GetMapping(path="/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Flight not found!")
            ),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<Flight>> getAllFlights() {
        return new ResponseEntity<>(flightDB.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createFlight(@RequestBody Flight flight) {

        try {
            return new ResponseEntity<>(flightDB.save(flight), HttpStatus.CREATED);
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

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable int id, @RequestBody Flight flightDetails) throws ResponseStatusException {
        Flight flight = flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found!")
        );

        if (flightDetails.getRoute() != null) flight.setRoute(flightDetails.getRoute());
        if (flightDetails.getAirplane() != null) flight.setAirplane(flightDetails.getAirplane());
        if (flightDetails.getDepartureTime() != null) flight.setDepartureTime(flightDetails.getDepartureTime());
        if (flightDetails.getReservedSeats() != null) flight.setReservedSeats(flightDetails.getReservedSeats());
        if (flightDetails.getSeatPrice() != null) flight.setSeatPrice(flightDetails.getSeatPrice());

        try {
            Flight updatedFlight = flightDB.save(flight);
            return new ResponseEntity<>(updatedFlight, HttpStatus.NO_CONTENT);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable int id) throws ResponseStatusException {
        Flight flight = flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight could not be found!"));

        try {
            flightDB.delete(flight);
            return new ResponseEntity<>(flight, HttpStatus.NO_CONTENT);
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
