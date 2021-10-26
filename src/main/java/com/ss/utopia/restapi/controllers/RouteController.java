package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.RouteRepository;
import com.ss.utopia.restapi.models.Route;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/routes")
public class RouteController {

    @Autowired
    RouteRepository routeDB;

    @Autowired
    ResetAutoCounterService resetService;

    @GetMapping(path="/{id}")
    public Route getRoute(@PathVariable int id) throws ResponseStatusException {
        return routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Route not found!")
            );
    }

    @GetMapping(path="/all")
    public Iterable<Route> getAllRoutes() {
        return routeDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createRoute(@RequestBody Route route) {
        resetService.resetAutoCounter("route");
        try {
            return new ResponseEntity<>(
                routeDB.save(route),
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
    public ResponseEntity<?> updateRoute(@PathVariable int id, @RequestBody Route routeDetails) throws ResponseStatusException {
        Route route = routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Route not found!")
            );

        route.setOrigin(routeDetails.getOrigin());
        route.setDestination(routeDetails.getDestination());

        try {
            Route updatedRoute = routeDB.save(route);
            return new ResponseEntity<>(updatedRoute, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deleteRoute(@PathVariable int id) throws ResponseStatusException {
        Route route = routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Route could not be found!")
            );

        try {
            routeDB.delete(route);
            resetService.resetAutoCounter("route");
            return new ResponseEntity<>(route, HttpStatus.NO_CONTENT);
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
