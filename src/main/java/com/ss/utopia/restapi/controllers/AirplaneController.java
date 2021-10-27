package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirplaneRepository;
import com.ss.utopia.restapi.models.Airplane;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/airplanes")
public class AirplaneController {

    @Autowired
    AirplaneRepository airplaneDB;

    @Autowired
    ResetAutoCounterService resetService;

    @GetMapping(path="/{id}")
    public ResponseEntity<Airplane> getAirplane(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Airplane not found!")
            ),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<Airplane>> getAllAirplanes() {
        return new ResponseEntity<>(airplaneDB.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirplane(@RequestBody Airplane airplane) {
        resetService.resetAutoCounter("airplane");
        try {
            return new ResponseEntity<>(
                airplaneDB.save(airplane),
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

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateAirplane(@PathVariable int id, @RequestBody Airplane airplaneDetails) throws ResponseStatusException {
        Airplane airplane = airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airplane not found!")
        );

        airplane.setAirplaneType(airplaneDetails.getAirplaneType());

        try {
            Airplane updatedAirplane = airplaneDB.save(airplane);
            return new ResponseEntity<>(
                updatedAirplane,
                HttpStatus.NO_CONTENT
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirplane(@PathVariable int id) throws ResponseStatusException {
        Airplane airplane = airplaneDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Airplane could not be found!"));

        try {
            airplaneDB.delete(airplane);
            resetService.resetAutoCounter("airplane");
            return new ResponseEntity<>(
                airplane,
                HttpStatus.NO_CONTENT
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
}
