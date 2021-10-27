package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirplaneTypeRepository;
import com.ss.utopia.restapi.models.AirplaneType;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/airplane-types")
public class AirplaneTypeController {

    @Autowired
    AirplaneTypeRepository airplaneTypeDB;

    @Autowired
    ResetAutoCounterService resetService;

    @GetMapping(path="/{id}")
    public ResponseEntity<AirplaneType> getAirplaneType(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<AirplaneType>(airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "AirplaneType not found!")
            ),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<AirplaneType>> getAllAirplaneTypes() {
        return new ResponseEntity<Iterable<AirplaneType>>(
            airplaneTypeDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirplaneType(@RequestBody AirplaneType airplaneType) {
        resetService.resetAutoCounter("airplane_type");
        try {
            return new ResponseEntity<>(
                airplaneTypeDB.save(airplaneType),
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
    public ResponseEntity<?> updateAirplaneType(@PathVariable int id, @RequestBody AirplaneType airplaneTypeDetails) throws ResponseStatusException {
        AirplaneType airplaneType = airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "AirplaneType not found!")
        );

        airplaneType.setMaxCapacity(airplaneTypeDetails.getMaxCapacity());

        try {
            AirplaneType updatedAirplaneType = airplaneTypeDB.save(airplaneType);
            return new ResponseEntity<>(
                updatedAirplaneType,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirplaneType(@PathVariable int id) throws ResponseStatusException {
        AirplaneType airplaneType = airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "AirplaneType could not be found!"));

        try {
            airplaneTypeDB.delete(airplaneType);
            resetService.resetAutoCounter("airplane_type");
            return new ResponseEntity<>(airplaneType, HttpStatus.OK);
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
