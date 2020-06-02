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
 * Criteria class for the {@link com.yankevych.tickets.domain.Flight} entity. This class is used
 * in {@link com.yankevych.tickets.web.rest.FlightResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /flights?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlightCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter number;

    private ZonedDateTimeFilter departureTime;

    private ZonedDateTimeFilter arrivalTime;

    private LongFilter ticketsId;

    private LongFilter passengersId;

    private LongFilter toId;

    private LongFilter fromId;

    public FlightCriteria() {
    }

    public FlightCriteria(FlightCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.departureTime = other.departureTime == null ? null : other.departureTime.copy();
        this.arrivalTime = other.arrivalTime == null ? null : other.arrivalTime.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
        this.passengersId = other.passengersId == null ? null : other.passengersId.copy();
        this.toId = other.toId == null ? null : other.toId.copy();
        this.fromId = other.fromId == null ? null : other.fromId.copy();
    }

    @Override
    public FlightCriteria copy() {
        return new FlightCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public ZonedDateTimeFilter getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTimeFilter departureTime) {
        this.departureTime = departureTime;
    }

    public ZonedDateTimeFilter getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ZonedDateTimeFilter arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LongFilter getTicketsId() {
        return ticketsId;
    }

    public void setTicketsId(LongFilter ticketsId) {
        this.ticketsId = ticketsId;
    }

    public LongFilter getPassengersId() {
        return passengersId;
    }

    public void setPassengersId(LongFilter passengersId) {
        this.passengersId = passengersId;
    }

    public LongFilter getToId() {
        return toId;
    }

    public void setToId(LongFilter toId) {
        this.toId = toId;
    }

    public LongFilter getFromId() {
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FlightCriteria that = (FlightCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(departureTime, that.departureTime) &&
            Objects.equals(arrivalTime, that.arrivalTime) &&
            Objects.equals(ticketsId, that.ticketsId) &&
            Objects.equals(passengersId, that.passengersId) &&
            Objects.equals(toId, that.toId) &&
            Objects.equals(fromId, that.fromId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        number,
        departureTime,
        arrivalTime,
        ticketsId,
        passengersId,
        toId,
        fromId
        );
    }

    @Override
    public String toString() {
        return "FlightCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (departureTime != null ? "departureTime=" + departureTime + ", " : "") +
                (arrivalTime != null ? "arrivalTime=" + arrivalTime + ", " : "") +
                (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
                (passengersId != null ? "passengersId=" + passengersId + ", " : "") +
                (toId != null ? "toId=" + toId + ", " : "") +
                (fromId != null ? "fromId=" + fromId + ", " : "") +
            "}";
    }

}
