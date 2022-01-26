package com.ss.utopia.restapi.models;

import java.sql.Timestamp;

import javax.persistence.*;

import jdk.jfr.Unsigned;

@Entity(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Unsigned
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(name = "departure_time", nullable = false)
    private Timestamp departureTime;

    @Column(name = "reserved_seats", nullable = false)
    @Unsigned
    private Integer reservedSeats;

    @Column(name = "seat_price", nullable = false)
    private Float seatPrice;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Float getSeatPrice() { return seatPrice; }
    public void setSeatPrice(Float seatPrice) { this.seatPrice = seatPrice; }

    public Integer getReservedSeats() { return reservedSeats; }
    public void setReservedSeats(Integer reservedSeats) { this.reservedSeats = reservedSeats; }

    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }

    public Airplane getAirplane() { return airplane; }
    public void setAirplane(Airplane airplane) { this.airplane = airplane; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
}
