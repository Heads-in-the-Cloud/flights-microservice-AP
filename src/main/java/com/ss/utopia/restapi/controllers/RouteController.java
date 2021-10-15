package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.RouteRepository;
import com.ss.utopia.restapi.models.Route;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/routes")
public class RouteController {

    @Autowired
    RouteRepository routeDB;

    @GetMapping(path="/{id}")
    public Route getRoute(@PathVariable int id) throws ResponseStatusException {
        return routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route not found!"));
    }

    @GetMapping(path="/all")
    public Iterable<Route> getAllRoutes() {
        return routeDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createRoute(@RequestBody Route route) {
        return new ResponseEntity<>(routeDB.save(route), HttpStatus.OK);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable int id, @RequestBody Route routeDetails) throws ResponseStatusException {
        Route route = routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route not found!")
        );

        route.setOrigin(routeDetails.getOrigin());
        route.setDestination(routeDetails.getDestination());

        Route updatedRoute = routeDB.save(route);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable int id) throws ResponseStatusException {
        Route route = routeDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route could not be found!"));

        routeDB.delete(route);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }
}
