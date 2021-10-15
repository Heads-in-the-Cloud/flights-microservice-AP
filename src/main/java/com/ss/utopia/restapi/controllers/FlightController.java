package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.FlightRepository;
import com.ss.utopia.restapi.models.Flight;

import org.springframework.beans.factory.annotation.*;
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
    public Flight getFlight(@PathVariable int id) throws ResponseStatusException {
        return flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found!"));
    }

    @GetMapping(path="/all")
    public Iterable<Flight> getAllFlights() {
        return flightDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createFlight(@RequestBody Flight flight) {
        return new ResponseEntity<>(flightDB.save(flight), HttpStatus.OK);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable int id, @RequestBody Flight flightDetails) throws ResponseStatusException {
        Flight flight = flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found!")
        );

        flight.setRoute(flightDetails.getRoute());
        flight.setAirplane(flightDetails.getAirplane());
        flight.setDepartureTime(flightDetails.getDepartureTime());
        flight.setReservedSeats(flightDetails.getReservedSeats());
        flight.setSeatPrice(flightDetails.getSeatPrice());

        Flight updatedFlight = flightDB.save(flight);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable int id) throws ResponseStatusException {
        Flight flight = flightDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight could not be found!"));

        flightDB.delete(flight);
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }
}
