package com.yankevych.tickets.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.yankevych.tickets.domain.Ticket} entity. This class is used
 * in {@link com.yankevych.tickets.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TicketCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter flightNumber;

    private StringFilter username;

    private ZonedDateTimeFilter purchased;

    private StringFilter reservationId;

    private StringFilter seatNumber;

    private DoubleFilter maxKg;

    private DoubleFilter price;

    private LongFilter userId;

    private LongFilter flightId;

    public TicketCriteria() {
    }

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.flightNumber = other.flightNumber == null ? null : other.flightNumber.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.purchased = other.purchased == null ? null : other.purchased.copy();
        this.reservationId = other.reservationId == null ? null : other.reservationId.copy();
        this.seatNumber = other.seatNumber == null ? null : other.seatNumber.copy();
        this.maxKg = other.maxKg == null ? null : other.maxKg.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.flightId = other.flightId == null ? null : other.flightId.copy();
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(StringFilter flightNumber) {
        this.flightNumber = flightNumber;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public ZonedDateTimeFilter getPurchased() {
        return purchased;
    }

    public void setPurchased(ZonedDateTimeFilter purchased) {
        this.purchased = purchased;
    }

    public StringFilter getReservationId() {
        return reservationId;
    }

    public void setReservationId(StringFilter reservationId) {
        this.reservationId = reservationId;
    }

    public StringFilter getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(StringFilter seatNumber) {
        this.seatNumber = seatNumber;
    }

    public DoubleFilter getMaxKg() {
        return maxKg;
    }

    public void setMaxKg(DoubleFilter maxKg) {
        this.maxKg = maxKg;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getFlightId() {
        return flightId;
    }

    public void setFlightId(LongFilter flightId) {
        this.flightId = flightId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(flightNumber, that.flightNumber) &&
            Objects.equals(username, that.username) &&
            Objects.equals(purchased, that.purchased) &&
            Objects.equals(reservationId, that.reservationId) &&
            Objects.equals(seatNumber, that.seatNumber) &&
            Objects.equals(maxKg, that.maxKg) &&
            Objects.equals(price, that.price) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        flightNumber,
        username,
        purchased,
        reservationId,
        seatNumber,
        maxKg,
        price,
        userId,
        flightId
        );
    }

    @Override
    public String toString() {
        return "TicketCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (flightNumber != null ? "flightNumber=" + flightNumber + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (purchased != null ? "purchased=" + purchased + ", " : "") +
                (reservationId != null ? "reservationId=" + reservationId + ", " : "") +
                (seatNumber != null ? "seatNumber=" + seatNumber + ", " : "") +
                (maxKg != null ? "maxKg=" + maxKg + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (flightId != null ? "flightId=" + flightId + ", " : "") +
            "}";
    }

}
