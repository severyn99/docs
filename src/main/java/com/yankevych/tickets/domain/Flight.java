package com.yankevych.tickets.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "departure_time")
    private ZonedDateTime departureTime;

    @Column(name = "arrival_time")
    private ZonedDateTime arrivalTime;

    @OneToMany(mappedBy = "flight")
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(mappedBy = "flight")
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("flights")
    private City to;

    @ManyToOne
    @JsonIgnoreProperties("flights")
    private City from;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public Flight number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public Flight departureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Flight arrivalTime(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public void setArrivalTime(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public Flight tickets(Set<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public Flight addTickets(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setFlight(this);
        return this;
    }

    public Flight removeTickets(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setFlight(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Flight passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Flight addPassengers(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.setFlight(this);
        return this;
    }

    public Flight removePassengers(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setFlight(null);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public City getTo() {
        return to;
    }

    public Flight to(City city) {
        this.to = city;
        return this;
    }

    public void setTo(City city) {
        this.to = city;
    }

    public City getFrom() {
        return from;
    }

    public Flight from(City city) {
        this.from = city;
        return this;
    }

    public void setFrom(City city) {
        this.from = city;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flight)) {
            return false;
        }
        return id != null && id.equals(((Flight) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Flight{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", departureTime='" + getDepartureTime() + "'" +
            ", arrivalTime='" + getArrivalTime() + "'" +
            "}";
    }
}
