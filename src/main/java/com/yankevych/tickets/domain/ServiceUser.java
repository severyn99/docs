package com.yankevych.tickets.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceUser.
 */
@Entity
@Table(name = "service_user")
public class ServiceUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone")
    private String phone;

    @Column(name = "credit_card")
    private String creditCard;

    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public ServiceUser username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public ServiceUser age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public ServiceUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public ServiceUser creditCard(String creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public ServiceUser tickets(Set<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public ServiceUser addTickets(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setUser(this);
        return this;
    }

    public ServiceUser removeTickets(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setUser(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public ServiceUser payments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public ServiceUser addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setUser(this);
        return this;
    }

    public ServiceUser removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setUser(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceUser)) {
            return false;
        }
        return id != null && id.equals(((ServiceUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", age=" + getAge() +
            ", phone='" + getPhone() + "'" +
            ", creditCard='" + getCreditCard() + "'" +
            "}";
    }
}
