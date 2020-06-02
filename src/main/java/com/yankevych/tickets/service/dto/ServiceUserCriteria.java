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

/**
 * Criteria class for the {@link com.yankevych.tickets.domain.ServiceUser} entity. This class is used
 * in {@link com.yankevych.tickets.web.rest.ServiceUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServiceUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private IntegerFilter age;

    private StringFilter phone;

    private StringFilter creditCard;

    private LongFilter ticketsId;

    private LongFilter paymentsId;

    public ServiceUserCriteria() {
    }

    public ServiceUserCriteria(ServiceUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.creditCard = other.creditCard == null ? null : other.creditCard.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
        this.paymentsId = other.paymentsId == null ? null : other.paymentsId.copy();
    }

    @Override
    public ServiceUserCriteria copy() {
        return new ServiceUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(StringFilter creditCard) {
        this.creditCard = creditCard;
    }

    public LongFilter getTicketsId() {
        return ticketsId;
    }

    public void setTicketsId(LongFilter ticketsId) {
        this.ticketsId = ticketsId;
    }

    public LongFilter getPaymentsId() {
        return paymentsId;
    }

    public void setPaymentsId(LongFilter paymentsId) {
        this.paymentsId = paymentsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServiceUserCriteria that = (ServiceUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(age, that.age) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(creditCard, that.creditCard) &&
            Objects.equals(ticketsId, that.ticketsId) &&
            Objects.equals(paymentsId, that.paymentsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        username,
        age,
        phone,
        creditCard,
        ticketsId,
        paymentsId
        );
    }

    @Override
    public String toString() {
        return "ServiceUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (creditCard != null ? "creditCard=" + creditCard + ", " : "") +
                (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
                (paymentsId != null ? "paymentsId=" + paymentsId + ", " : "") +
            "}";
    }

}
