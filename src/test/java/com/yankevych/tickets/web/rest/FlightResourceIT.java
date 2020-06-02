package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.TicketsApp;
import com.yankevych.tickets.domain.Flight;
import com.yankevych.tickets.domain.Ticket;
import com.yankevych.tickets.domain.Passenger;
import com.yankevych.tickets.domain.City;
import com.yankevych.tickets.repository.FlightRepository;
import com.yankevych.tickets.service.FlightService;
import com.yankevych.tickets.service.dto.FlightCriteria;
import com.yankevych.tickets.service.FlightQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.yankevych.tickets.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FlightResource} REST controller.
 */
@SpringBootTest(classes = TicketsApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class FlightResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEPARTURE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DEPARTURE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ARRIVAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ARRIVAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightQueryService flightQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlightMockMvc;

    private Flight flight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight()
            .number(DEFAULT_NUMBER)
            .departureTime(DEFAULT_DEPARTURE_TIME)
            .arrivalTime(DEFAULT_ARRIVAL_TIME);
        return flight;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createUpdatedEntity(EntityManager em) {
        Flight flight = new Flight()
            .number(UPDATED_NUMBER)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME);
        return flight;
    }

    @BeforeEach
    public void initTest() {
        flight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testFlight.getDepartureTime()).isEqualTo(DEFAULT_DEPARTURE_TIME);
        assertThat(testFlight.getArrivalTime()).isEqualTo(DEFAULT_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void createFlightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight with an existing ID
        flight.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(sameInstant(DEFAULT_DEPARTURE_TIME))))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(sameInstant(DEFAULT_ARRIVAL_TIME))));
    }
    
    @Test
    @Transactional
    public void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.departureTime").value(sameInstant(DEFAULT_DEPARTURE_TIME)))
            .andExpect(jsonPath("$.arrivalTime").value(sameInstant(DEFAULT_ARRIVAL_TIME)));
    }


    @Test
    @Transactional
    public void getFlightsByIdFiltering() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        Long id = flight.getId();

        defaultFlightShouldBeFound("id.equals=" + id);
        defaultFlightShouldNotBeFound("id.notEquals=" + id);

        defaultFlightShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlightShouldNotBeFound("id.greaterThan=" + id);

        defaultFlightShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlightShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFlightsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number equals to DEFAULT_NUMBER
        defaultFlightShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the flightList where number equals to UPDATED_NUMBER
        defaultFlightShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFlightsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number not equals to DEFAULT_NUMBER
        defaultFlightShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the flightList where number not equals to UPDATED_NUMBER
        defaultFlightShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFlightsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultFlightShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the flightList where number equals to UPDATED_NUMBER
        defaultFlightShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFlightsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number is not null
        defaultFlightShouldBeFound("number.specified=true");

        // Get all the flightList where number is null
        defaultFlightShouldNotBeFound("number.specified=false");
    }
                @Test
    @Transactional
    public void getAllFlightsByNumberContainsSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number contains DEFAULT_NUMBER
        defaultFlightShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the flightList where number contains UPDATED_NUMBER
        defaultFlightShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFlightsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where number does not contain DEFAULT_NUMBER
        defaultFlightShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the flightList where number does not contain UPDATED_NUMBER
        defaultFlightShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime equals to DEFAULT_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.equals=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime equals to UPDATED_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.equals=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime not equals to DEFAULT_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.notEquals=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime not equals to UPDATED_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.notEquals=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsInShouldWork() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime in DEFAULT_DEPARTURE_TIME or UPDATED_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.in=" + DEFAULT_DEPARTURE_TIME + "," + UPDATED_DEPARTURE_TIME);

        // Get all the flightList where departureTime equals to UPDATED_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.in=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime is not null
        defaultFlightShouldBeFound("departureTime.specified=true");

        // Get all the flightList where departureTime is null
        defaultFlightShouldNotBeFound("departureTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime is greater than or equal to DEFAULT_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.greaterThanOrEqual=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime is greater than or equal to UPDATED_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.greaterThanOrEqual=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime is less than or equal to DEFAULT_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.lessThanOrEqual=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime is less than or equal to SMALLER_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.lessThanOrEqual=" + SMALLER_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime is less than DEFAULT_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.lessThan=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime is less than UPDATED_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.lessThan=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByDepartureTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where departureTime is greater than DEFAULT_DEPARTURE_TIME
        defaultFlightShouldNotBeFound("departureTime.greaterThan=" + DEFAULT_DEPARTURE_TIME);

        // Get all the flightList where departureTime is greater than SMALLER_DEPARTURE_TIME
        defaultFlightShouldBeFound("departureTime.greaterThan=" + SMALLER_DEPARTURE_TIME);
    }


    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime equals to DEFAULT_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.equals=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime equals to UPDATED_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.equals=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime not equals to DEFAULT_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.notEquals=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime not equals to UPDATED_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.notEquals=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsInShouldWork() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime in DEFAULT_ARRIVAL_TIME or UPDATED_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.in=" + DEFAULT_ARRIVAL_TIME + "," + UPDATED_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime equals to UPDATED_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.in=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime is not null
        defaultFlightShouldBeFound("arrivalTime.specified=true");

        // Get all the flightList where arrivalTime is null
        defaultFlightShouldNotBeFound("arrivalTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime is greater than or equal to DEFAULT_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.greaterThanOrEqual=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime is greater than or equal to UPDATED_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.greaterThanOrEqual=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime is less than or equal to DEFAULT_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.lessThanOrEqual=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime is less than or equal to SMALLER_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.lessThanOrEqual=" + SMALLER_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime is less than DEFAULT_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.lessThan=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime is less than UPDATED_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.lessThan=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllFlightsByArrivalTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList where arrivalTime is greater than DEFAULT_ARRIVAL_TIME
        defaultFlightShouldNotBeFound("arrivalTime.greaterThan=" + DEFAULT_ARRIVAL_TIME);

        // Get all the flightList where arrivalTime is greater than SMALLER_ARRIVAL_TIME
        defaultFlightShouldBeFound("arrivalTime.greaterThan=" + SMALLER_ARRIVAL_TIME);
    }


    @Test
    @Transactional
    public void getAllFlightsByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        Ticket tickets = TicketResourceIT.createEntity(em);
        em.persist(tickets);
        em.flush();
        flight.addTickets(tickets);
        flightRepository.saveAndFlush(flight);
        Long ticketsId = tickets.getId();

        // Get all the flightList where tickets equals to ticketsId
        defaultFlightShouldBeFound("ticketsId.equals=" + ticketsId);

        // Get all the flightList where tickets equals to ticketsId + 1
        defaultFlightShouldNotBeFound("ticketsId.equals=" + (ticketsId + 1));
    }


    @Test
    @Transactional
    public void getAllFlightsByPassengersIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        Passenger passengers = PassengerResourceIT.createEntity(em);
        em.persist(passengers);
        em.flush();
        flight.addPassengers(passengers);
        flightRepository.saveAndFlush(flight);
        Long passengersId = passengers.getId();

        // Get all the flightList where passengers equals to passengersId
        defaultFlightShouldBeFound("passengersId.equals=" + passengersId);

        // Get all the flightList where passengers equals to passengersId + 1
        defaultFlightShouldNotBeFound("passengersId.equals=" + (passengersId + 1));
    }


    @Test
    @Transactional
    public void getAllFlightsByToIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        City to = CityResourceIT.createEntity(em);
        em.persist(to);
        em.flush();
        flight.setTo(to);
        flightRepository.saveAndFlush(flight);
        Long toId = to.getId();

        // Get all the flightList where to equals to toId
        defaultFlightShouldBeFound("toId.equals=" + toId);

        // Get all the flightList where to equals to toId + 1
        defaultFlightShouldNotBeFound("toId.equals=" + (toId + 1));
    }


    @Test
    @Transactional
    public void getAllFlightsByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        City from = CityResourceIT.createEntity(em);
        em.persist(from);
        em.flush();
        flight.setFrom(from);
        flightRepository.saveAndFlush(flight);
        Long fromId = from.getId();

        // Get all the flightList where from equals to fromId
        defaultFlightShouldBeFound("fromId.equals=" + fromId);

        // Get all the flightList where from equals to fromId + 1
        defaultFlightShouldNotBeFound("fromId.equals=" + (fromId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlightShouldBeFound(String filter) throws Exception {
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(sameInstant(DEFAULT_DEPARTURE_TIME))))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(sameInstant(DEFAULT_ARRIVAL_TIME))));

        // Check, that the count call also returns 1
        restFlightMockMvc.perform(get("/api/flights/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlightShouldNotBeFound(String filter) throws Exception {
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlightMockMvc.perform(get("/api/flights/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlight() throws Exception {
        // Initialize the database
        flightService.save(flight);

        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findById(flight.getId()).get();
        // Disconnect from session so that the updates on updatedFlight are not directly saved in db
        em.detach(updatedFlight);
        updatedFlight
            .number(UPDATED_NUMBER)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME);

        restFlightMockMvc.perform(put("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlight)))
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testFlight.getDepartureTime()).isEqualTo(UPDATED_DEPARTURE_TIME);
        assertThat(testFlight.getArrivalTime()).isEqualTo(UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Create the Flight

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightMockMvc.perform(put("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFlight() throws Exception {
        // Initialize the database
        flightService.save(flight);

        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Delete the flight
        restFlightMockMvc.perform(delete("/api/flights/{id}", flight.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
