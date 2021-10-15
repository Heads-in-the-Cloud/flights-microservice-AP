package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AirplaneTypeRepository;
import com.ss.utopia.restapi.models.AirplaneType;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/airplane-types")
public class AirplaneTypeController {

    @Autowired
    AirplaneTypeRepository airplaneTypeDB;

    @GetMapping(path="/{id}")
    public AirplaneType getAirplaneType(@PathVariable int id) throws ResponseStatusException {
        return airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "AirplaneType not found!"));
    }

    @GetMapping(path="/all")
    public Iterable<AirplaneType> getAllAirplaneTypes() {
        return airplaneTypeDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createAirplaneType(@RequestBody AirplaneType airplaneType) {
        return new ResponseEntity<>(airplaneTypeDB.save(airplaneType), HttpStatus.OK);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateAirplaneType(@PathVariable int id, @RequestBody AirplaneType airplaneTypeDetails) throws ResponseStatusException {
        AirplaneType airplaneType = airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "AirplaneType not found!")
        );

        airplaneType.setMaxCapacity(airplaneTypeDetails.getMaxCapacity());

        AirplaneType updatedAirplaneType = airplaneTypeDB.save(airplaneType);
        return new ResponseEntity<>(updatedAirplaneType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirplaneType(@PathVariable int id) throws ResponseStatusException {
        AirplaneType airplaneType = airplaneTypeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "AirplaneType could not be found!"));

        airplaneTypeDB.delete(airplaneType);
        return new ResponseEntity<>(airplaneType, HttpStatus.OK);
    }
}
