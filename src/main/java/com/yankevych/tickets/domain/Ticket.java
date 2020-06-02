package com.yankevych.tickets.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "username")
    private String username;

    @Column(name = "purchased")
    private ZonedDateTime purchased;

    @Column(name = "reservation_id")
    private String reservationId;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "max_kg")
    private Double maxKg;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JsonIgnoreProperties("tickets")
    private ServiceUser user;

    @ManyToOne
    @JsonIgnoreProperties("tickets")
    private Flight flight;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Ticket flightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getUsername() {
        return username;
    }

    public Ticket username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZonedDateTime getPurchased() {
        return purchased;
    }

    public Ticket purchased(ZonedDateTime purchased) {
        this.purchased = purchased;
        return this;
    }

    public void setPurchased(ZonedDateTime purchased) {
        this.purchased = purchased;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Ticket reservationId(String reservationId) {
        this.reservationId = reservationId;
        return this;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public Ticket seatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
        return this;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Double getMaxKg() {
        return maxKg;
    }

    public Ticket maxKg(Double maxKg) {
        this.maxKg = maxKg;
        return this;
    }

    public void setMaxKg(Double maxKg) {
        this.maxKg = maxKg;
    }

    public Double getPrice() {
        return price;
    }

    public Ticket price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ServiceUser getUser() {
        return user;
    }

    public Ticket user(ServiceUser serviceUser) {
        this.user = serviceUser;
        return this;
    }

    public void setUser(ServiceUser serviceUser) {
        this.user = serviceUser;
    }

    public Flight getFlight() {
        return flight;
    }

    public Ticket flight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", flightNumber='" + getFlightNumber() + "'" +
            ", username='" + getUsername() + "'" +
            ", purchased='" + getPurchased() + "'" +
            ", reservationId='" + getReservationId() + "'" +
            ", seatNumber='" + getSeatNumber() + "'" +
            ", maxKg=" + getMaxKg() +
            ", price=" + getPrice() +
            "}";
    }
}
