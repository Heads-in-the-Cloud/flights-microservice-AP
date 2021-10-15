package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirplaneRepository;
import com.ss.utopia.restapi.models.Airplane;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/airplanes")
public class AirplaneController {

    @Autowired
    AirplaneRepository airplaneDB;

    @GetMapping(path="/{id}")
    public Airplane getAirplane(@PathVariable int id) throws ResponseStatusException {
        return airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airplane not found!"));
    }

    @GetMapping(path="/all")
    public Iterable<Airplane> getAllAirplanes() {
        return airplaneDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirplane(@RequestBody Airplane airplane) {
        return new ResponseEntity<>(airplaneDB.save(airplane), HttpStatus.OK);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateAirplane(@PathVariable int id, @RequestBody Airplane airplaneDetails) throws ResponseStatusException {
        Airplane airplane = airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airplane not found!")
        );

        airplane.setAirplaneType(airplaneDetails.getAirplaneType());
        airplane.setFlights(airplaneDetails.getFlights());

        Airplane updatedAirplane = airplaneDB.save(airplane);
        return new ResponseEntity<>(updatedAirplane, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirplane(@PathVariable int id) throws ResponseStatusException {
        Airplane airplane = airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airplane could not be found!"));

        airplaneDB.delete(airplane);
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }
}
