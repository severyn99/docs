package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.TicketsApp;
import com.yankevych.tickets.domain.Passenger;
import com.yankevych.tickets.domain.Flight;
import com.yankevych.tickets.repository.PassengerRepository;
import com.yankevych.tickets.service.PassengerService;
import com.yankevych.tickets.service.dto.PassengerCriteria;
import com.yankevych.tickets.service.PassengerQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@SpringBootTest(classes = TicketsApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PassengerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PassengerQueryService passengerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE);
        return passenger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE);
        return passenger;
    }

    @BeforeEach
    public void initTest() {
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPassenger.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createPassengerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger with an existing ID
        passenger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
    
    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }


    @Test
    @Transactional
    public void getPassengersByIdFiltering() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        Long id = passenger.getId();

        defaultPassengerShouldBeFound("id.equals=" + id);
        defaultPassengerShouldNotBeFound("id.notEquals=" + id);

        defaultPassengerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPassengerShouldNotBeFound("id.greaterThan=" + id);

        defaultPassengerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPassengerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPassengersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name equals to DEFAULT_NAME
        defaultPassengerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the passengerList where name equals to UPDATED_NAME
        defaultPassengerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPassengersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name not equals to DEFAULT_NAME
        defaultPassengerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the passengerList where name not equals to UPDATED_NAME
        defaultPassengerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPassengersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPassengerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the passengerList where name equals to UPDATED_NAME
        defaultPassengerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPassengersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name is not null
        defaultPassengerShouldBeFound("name.specified=true");

        // Get all the passengerList where name is null
        defaultPassengerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPassengersByNameContainsSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name contains DEFAULT_NAME
        defaultPassengerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the passengerList where name contains UPDATED_NAME
        defaultPassengerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPassengersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where name does not contain DEFAULT_NAME
        defaultPassengerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the passengerList where name does not contain UPDATED_NAME
        defaultPassengerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPassengersByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age equals to DEFAULT_AGE
        defaultPassengerShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the passengerList where age equals to UPDATED_AGE
        defaultPassengerShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age not equals to DEFAULT_AGE
        defaultPassengerShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the passengerList where age not equals to UPDATED_AGE
        defaultPassengerShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age in DEFAULT_AGE or UPDATED_AGE
        defaultPassengerShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the passengerList where age equals to UPDATED_AGE
        defaultPassengerShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age is not null
        defaultPassengerShouldBeFound("age.specified=true");

        // Get all the passengerList where age is null
        defaultPassengerShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age is greater than or equal to DEFAULT_AGE
        defaultPassengerShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the passengerList where age is greater than or equal to UPDATED_AGE
        defaultPassengerShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age is less than or equal to DEFAULT_AGE
        defaultPassengerShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the passengerList where age is less than or equal to SMALLER_AGE
        defaultPassengerShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age is less than DEFAULT_AGE
        defaultPassengerShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the passengerList where age is less than UPDATED_AGE
        defaultPassengerShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPassengersByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList where age is greater than DEFAULT_AGE
        defaultPassengerShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the passengerList where age is greater than SMALLER_AGE
        defaultPassengerShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }


    @Test
    @Transactional
    public void getAllPassengersByFlightIsEqualToSomething() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);
        Flight flight = FlightResourceIT.createEntity(em);
        em.persist(flight);
        em.flush();
        passenger.setFlight(flight);
        passengerRepository.saveAndFlush(passenger);
        Long flightId = flight.getId();

        // Get all the passengerList where flight equals to flightId
        defaultPassengerShouldBeFound("flightId.equals=" + flightId);

        // Get all the passengerList where flight equals to flightId + 1
        defaultPassengerShouldNotBeFound("flightId.equals=" + (flightId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPassengerShouldBeFound(String filter) throws Exception {
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));

        // Check, that the count call also returns 1
        restPassengerMockMvc.perform(get("/api/passengers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPassengerShouldNotBeFound(String filter) throws Exception {
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPassengerMockMvc.perform(get("/api/passengers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerService.save(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).get();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .name(UPDATED_NAME)
            .age(UPDATED_AGE);

        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassenger)))
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPassenger.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Create the Passenger

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerService.save(passenger);

        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Delete the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
