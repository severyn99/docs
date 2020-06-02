package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.TicketsApp;
import com.yankevych.tickets.domain.Ticket;
import com.yankevych.tickets.domain.ServiceUser;
import com.yankevych.tickets.domain.Flight;
import com.yankevych.tickets.repository.TicketRepository;
import com.yankevych.tickets.service.TicketService;
import com.yankevych.tickets.service.dto.TicketCriteria;
import com.yankevych.tickets.service.TicketQueryService;

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
 * Integration tests for the {@link TicketResource} REST controller.
 */
@SpringBootTest(classes = TicketsApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TicketResourceIT {

    private static final String DEFAULT_FLIGHT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FLIGHT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PURCHASED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PURCHASED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PURCHASED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_RESERVATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SEAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_MAX_KG = 1D;
    private static final Double UPDATED_MAX_KG = 2D;
    private static final Double SMALLER_MAX_KG = 1D - 1D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketQueryService ticketQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .flightNumber(DEFAULT_FLIGHT_NUMBER)
            .username(DEFAULT_USERNAME)
            .purchased(DEFAULT_PURCHASED)
            .reservationId(DEFAULT_RESERVATION_ID)
            .seatNumber(DEFAULT_SEAT_NUMBER)
            .maxKg(DEFAULT_MAX_KG)
            .price(DEFAULT_PRICE);
        return ticket;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .username(UPDATED_USERNAME)
            .purchased(UPDATED_PURCHASED)
            .reservationId(UPDATED_RESERVATION_ID)
            .seatNumber(UPDATED_SEAT_NUMBER)
            .maxKg(UPDATED_MAX_KG)
            .price(UPDATED_PRICE);
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket
        restTicketMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getFlightNumber()).isEqualTo(DEFAULT_FLIGHT_NUMBER);
        assertThat(testTicket.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicket.getPurchased()).isEqualTo(DEFAULT_PURCHASED);
        assertThat(testTicket.getReservationId()).isEqualTo(DEFAULT_RESERVATION_ID);
        assertThat(testTicket.getSeatNumber()).isEqualTo(DEFAULT_SEAT_NUMBER);
        assertThat(testTicket.getMaxKg()).isEqualTo(DEFAULT_MAX_KG);
        assertThat(testTicket.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createTicketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket with an existing ID
        ticket.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].flightNumber").value(hasItem(DEFAULT_FLIGHT_NUMBER)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].purchased").value(hasItem(sameInstant(DEFAULT_PURCHASED))))
            .andExpect(jsonPath("$.[*].reservationId").value(hasItem(DEFAULT_RESERVATION_ID)))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)))
            .andExpect(jsonPath("$.[*].maxKg").value(hasItem(DEFAULT_MAX_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.flightNumber").value(DEFAULT_FLIGHT_NUMBER))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.purchased").value(sameInstant(DEFAULT_PURCHASED)))
            .andExpect(jsonPath("$.reservationId").value(DEFAULT_RESERVATION_ID))
            .andExpect(jsonPath("$.seatNumber").value(DEFAULT_SEAT_NUMBER))
            .andExpect(jsonPath("$.maxKg").value(DEFAULT_MAX_KG.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }


    @Test
    @Transactional
    public void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketShouldBeFound("id.equals=" + id);
        defaultTicketShouldNotBeFound("id.notEquals=" + id);

        defaultTicketShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTicketsByFlightNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber equals to DEFAULT_FLIGHT_NUMBER
        defaultTicketShouldBeFound("flightNumber.equals=" + DEFAULT_FLIGHT_NUMBER);

        // Get all the ticketList where flightNumber equals to UPDATED_FLIGHT_NUMBER
        defaultTicketShouldNotBeFound("flightNumber.equals=" + UPDATED_FLIGHT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsByFlightNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber not equals to DEFAULT_FLIGHT_NUMBER
        defaultTicketShouldNotBeFound("flightNumber.notEquals=" + DEFAULT_FLIGHT_NUMBER);

        // Get all the ticketList where flightNumber not equals to UPDATED_FLIGHT_NUMBER
        defaultTicketShouldBeFound("flightNumber.notEquals=" + UPDATED_FLIGHT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsByFlightNumberIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber in DEFAULT_FLIGHT_NUMBER or UPDATED_FLIGHT_NUMBER
        defaultTicketShouldBeFound("flightNumber.in=" + DEFAULT_FLIGHT_NUMBER + "," + UPDATED_FLIGHT_NUMBER);

        // Get all the ticketList where flightNumber equals to UPDATED_FLIGHT_NUMBER
        defaultTicketShouldNotBeFound("flightNumber.in=" + UPDATED_FLIGHT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsByFlightNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber is not null
        defaultTicketShouldBeFound("flightNumber.specified=true");

        // Get all the ticketList where flightNumber is null
        defaultTicketShouldNotBeFound("flightNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketsByFlightNumberContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber contains DEFAULT_FLIGHT_NUMBER
        defaultTicketShouldBeFound("flightNumber.contains=" + DEFAULT_FLIGHT_NUMBER);

        // Get all the ticketList where flightNumber contains UPDATED_FLIGHT_NUMBER
        defaultTicketShouldNotBeFound("flightNumber.contains=" + UPDATED_FLIGHT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsByFlightNumberNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where flightNumber does not contain DEFAULT_FLIGHT_NUMBER
        defaultTicketShouldNotBeFound("flightNumber.doesNotContain=" + DEFAULT_FLIGHT_NUMBER);

        // Get all the ticketList where flightNumber does not contain UPDATED_FLIGHT_NUMBER
        defaultTicketShouldBeFound("flightNumber.doesNotContain=" + UPDATED_FLIGHT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllTicketsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username equals to DEFAULT_USERNAME
        defaultTicketShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the ticketList where username equals to UPDATED_USERNAME
        defaultTicketShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllTicketsByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username not equals to DEFAULT_USERNAME
        defaultTicketShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the ticketList where username not equals to UPDATED_USERNAME
        defaultTicketShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllTicketsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultTicketShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the ticketList where username equals to UPDATED_USERNAME
        defaultTicketShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllTicketsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username is not null
        defaultTicketShouldBeFound("username.specified=true");

        // Get all the ticketList where username is null
        defaultTicketShouldNotBeFound("username.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username contains DEFAULT_USERNAME
        defaultTicketShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the ticketList where username contains UPDATED_USERNAME
        defaultTicketShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllTicketsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where username does not contain DEFAULT_USERNAME
        defaultTicketShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the ticketList where username does not contain UPDATED_USERNAME
        defaultTicketShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }


    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased equals to DEFAULT_PURCHASED
        defaultTicketShouldBeFound("purchased.equals=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased equals to UPDATED_PURCHASED
        defaultTicketShouldNotBeFound("purchased.equals=" + UPDATED_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased not equals to DEFAULT_PURCHASED
        defaultTicketShouldNotBeFound("purchased.notEquals=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased not equals to UPDATED_PURCHASED
        defaultTicketShouldBeFound("purchased.notEquals=" + UPDATED_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased in DEFAULT_PURCHASED or UPDATED_PURCHASED
        defaultTicketShouldBeFound("purchased.in=" + DEFAULT_PURCHASED + "," + UPDATED_PURCHASED);

        // Get all the ticketList where purchased equals to UPDATED_PURCHASED
        defaultTicketShouldNotBeFound("purchased.in=" + UPDATED_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased is not null
        defaultTicketShouldBeFound("purchased.specified=true");

        // Get all the ticketList where purchased is null
        defaultTicketShouldNotBeFound("purchased.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased is greater than or equal to DEFAULT_PURCHASED
        defaultTicketShouldBeFound("purchased.greaterThanOrEqual=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased is greater than or equal to UPDATED_PURCHASED
        defaultTicketShouldNotBeFound("purchased.greaterThanOrEqual=" + UPDATED_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased is less than or equal to DEFAULT_PURCHASED
        defaultTicketShouldBeFound("purchased.lessThanOrEqual=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased is less than or equal to SMALLER_PURCHASED
        defaultTicketShouldNotBeFound("purchased.lessThanOrEqual=" + SMALLER_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased is less than DEFAULT_PURCHASED
        defaultTicketShouldNotBeFound("purchased.lessThan=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased is less than UPDATED_PURCHASED
        defaultTicketShouldBeFound("purchased.lessThan=" + UPDATED_PURCHASED);
    }

    @Test
    @Transactional
    public void getAllTicketsByPurchasedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where purchased is greater than DEFAULT_PURCHASED
        defaultTicketShouldNotBeFound("purchased.greaterThan=" + DEFAULT_PURCHASED);

        // Get all the ticketList where purchased is greater than SMALLER_PURCHASED
        defaultTicketShouldBeFound("purchased.greaterThan=" + SMALLER_PURCHASED);
    }


    @Test
    @Transactional
    public void getAllTicketsByReservationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId equals to DEFAULT_RESERVATION_ID
        defaultTicketShouldBeFound("reservationId.equals=" + DEFAULT_RESERVATION_ID);

        // Get all the ticketList where reservationId equals to UPDATED_RESERVATION_ID
        defaultTicketShouldNotBeFound("reservationId.equals=" + UPDATED_RESERVATION_ID);
    }

    @Test
    @Transactional
    public void getAllTicketsByReservationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId not equals to DEFAULT_RESERVATION_ID
        defaultTicketShouldNotBeFound("reservationId.notEquals=" + DEFAULT_RESERVATION_ID);

        // Get all the ticketList where reservationId not equals to UPDATED_RESERVATION_ID
        defaultTicketShouldBeFound("reservationId.notEquals=" + UPDATED_RESERVATION_ID);
    }

    @Test
    @Transactional
    public void getAllTicketsByReservationIdIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId in DEFAULT_RESERVATION_ID or UPDATED_RESERVATION_ID
        defaultTicketShouldBeFound("reservationId.in=" + DEFAULT_RESERVATION_ID + "," + UPDATED_RESERVATION_ID);

        // Get all the ticketList where reservationId equals to UPDATED_RESERVATION_ID
        defaultTicketShouldNotBeFound("reservationId.in=" + UPDATED_RESERVATION_ID);
    }

    @Test
    @Transactional
    public void getAllTicketsByReservationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId is not null
        defaultTicketShouldBeFound("reservationId.specified=true");

        // Get all the ticketList where reservationId is null
        defaultTicketShouldNotBeFound("reservationId.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketsByReservationIdContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId contains DEFAULT_RESERVATION_ID
        defaultTicketShouldBeFound("reservationId.contains=" + DEFAULT_RESERVATION_ID);

        // Get all the ticketList where reservationId contains UPDATED_RESERVATION_ID
        defaultTicketShouldNotBeFound("reservationId.contains=" + UPDATED_RESERVATION_ID);
    }

    @Test
    @Transactional
    public void getAllTicketsByReservationIdNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where reservationId does not contain DEFAULT_RESERVATION_ID
        defaultTicketShouldNotBeFound("reservationId.doesNotContain=" + DEFAULT_RESERVATION_ID);

        // Get all the ticketList where reservationId does not contain UPDATED_RESERVATION_ID
        defaultTicketShouldBeFound("reservationId.doesNotContain=" + UPDATED_RESERVATION_ID);
    }


    @Test
    @Transactional
    public void getAllTicketsBySeatNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber equals to DEFAULT_SEAT_NUMBER
        defaultTicketShouldBeFound("seatNumber.equals=" + DEFAULT_SEAT_NUMBER);

        // Get all the ticketList where seatNumber equals to UPDATED_SEAT_NUMBER
        defaultTicketShouldNotBeFound("seatNumber.equals=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsBySeatNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber not equals to DEFAULT_SEAT_NUMBER
        defaultTicketShouldNotBeFound("seatNumber.notEquals=" + DEFAULT_SEAT_NUMBER);

        // Get all the ticketList where seatNumber not equals to UPDATED_SEAT_NUMBER
        defaultTicketShouldBeFound("seatNumber.notEquals=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsBySeatNumberIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber in DEFAULT_SEAT_NUMBER or UPDATED_SEAT_NUMBER
        defaultTicketShouldBeFound("seatNumber.in=" + DEFAULT_SEAT_NUMBER + "," + UPDATED_SEAT_NUMBER);

        // Get all the ticketList where seatNumber equals to UPDATED_SEAT_NUMBER
        defaultTicketShouldNotBeFound("seatNumber.in=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsBySeatNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber is not null
        defaultTicketShouldBeFound("seatNumber.specified=true");

        // Get all the ticketList where seatNumber is null
        defaultTicketShouldNotBeFound("seatNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketsBySeatNumberContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber contains DEFAULT_SEAT_NUMBER
        defaultTicketShouldBeFound("seatNumber.contains=" + DEFAULT_SEAT_NUMBER);

        // Get all the ticketList where seatNumber contains UPDATED_SEAT_NUMBER
        defaultTicketShouldNotBeFound("seatNumber.contains=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTicketsBySeatNumberNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where seatNumber does not contain DEFAULT_SEAT_NUMBER
        defaultTicketShouldNotBeFound("seatNumber.doesNotContain=" + DEFAULT_SEAT_NUMBER);

        // Get all the ticketList where seatNumber does not contain UPDATED_SEAT_NUMBER
        defaultTicketShouldBeFound("seatNumber.doesNotContain=" + UPDATED_SEAT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg equals to DEFAULT_MAX_KG
        defaultTicketShouldBeFound("maxKg.equals=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg equals to UPDATED_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.equals=" + UPDATED_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg not equals to DEFAULT_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.notEquals=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg not equals to UPDATED_MAX_KG
        defaultTicketShouldBeFound("maxKg.notEquals=" + UPDATED_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg in DEFAULT_MAX_KG or UPDATED_MAX_KG
        defaultTicketShouldBeFound("maxKg.in=" + DEFAULT_MAX_KG + "," + UPDATED_MAX_KG);

        // Get all the ticketList where maxKg equals to UPDATED_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.in=" + UPDATED_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg is not null
        defaultTicketShouldBeFound("maxKg.specified=true");

        // Get all the ticketList where maxKg is null
        defaultTicketShouldNotBeFound("maxKg.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg is greater than or equal to DEFAULT_MAX_KG
        defaultTicketShouldBeFound("maxKg.greaterThanOrEqual=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg is greater than or equal to UPDATED_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.greaterThanOrEqual=" + UPDATED_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg is less than or equal to DEFAULT_MAX_KG
        defaultTicketShouldBeFound("maxKg.lessThanOrEqual=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg is less than or equal to SMALLER_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.lessThanOrEqual=" + SMALLER_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg is less than DEFAULT_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.lessThan=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg is less than UPDATED_MAX_KG
        defaultTicketShouldBeFound("maxKg.lessThan=" + UPDATED_MAX_KG);
    }

    @Test
    @Transactional
    public void getAllTicketsByMaxKgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where maxKg is greater than DEFAULT_MAX_KG
        defaultTicketShouldNotBeFound("maxKg.greaterThan=" + DEFAULT_MAX_KG);

        // Get all the ticketList where maxKg is greater than SMALLER_MAX_KG
        defaultTicketShouldBeFound("maxKg.greaterThan=" + SMALLER_MAX_KG);
    }


    @Test
    @Transactional
    public void getAllTicketsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price equals to DEFAULT_PRICE
        defaultTicketShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the ticketList where price equals to UPDATED_PRICE
        defaultTicketShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price not equals to DEFAULT_PRICE
        defaultTicketShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the ticketList where price not equals to UPDATED_PRICE
        defaultTicketShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultTicketShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the ticketList where price equals to UPDATED_PRICE
        defaultTicketShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price is not null
        defaultTicketShouldBeFound("price.specified=true");

        // Get all the ticketList where price is null
        defaultTicketShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price is greater than or equal to DEFAULT_PRICE
        defaultTicketShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the ticketList where price is greater than or equal to UPDATED_PRICE
        defaultTicketShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price is less than or equal to DEFAULT_PRICE
        defaultTicketShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the ticketList where price is less than or equal to SMALLER_PRICE
        defaultTicketShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price is less than DEFAULT_PRICE
        defaultTicketShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the ticketList where price is less than UPDATED_PRICE
        defaultTicketShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTicketsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where price is greater than DEFAULT_PRICE
        defaultTicketShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the ticketList where price is greater than SMALLER_PRICE
        defaultTicketShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllTicketsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        ServiceUser user = ServiceUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        ticket.setUser(user);
        ticketRepository.saveAndFlush(ticket);
        Long userId = user.getId();

        // Get all the ticketList where user equals to userId
        defaultTicketShouldBeFound("userId.equals=" + userId);

        // Get all the ticketList where user equals to userId + 1
        defaultTicketShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByFlightIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Flight flight = FlightResourceIT.createEntity(em);
        em.persist(flight);
        em.flush();
        ticket.setFlight(flight);
        ticketRepository.saveAndFlush(ticket);
        Long flightId = flight.getId();

        // Get all the ticketList where flight equals to flightId
        defaultTicketShouldBeFound("flightId.equals=" + flightId);

        // Get all the ticketList where flight equals to flightId + 1
        defaultTicketShouldNotBeFound("flightId.equals=" + (flightId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].flightNumber").value(hasItem(DEFAULT_FLIGHT_NUMBER)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].purchased").value(hasItem(sameInstant(DEFAULT_PURCHASED))))
            .andExpect(jsonPath("$.[*].reservationId").value(hasItem(DEFAULT_RESERVATION_ID)))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)))
            .andExpect(jsonPath("$.[*].maxKg").value(hasItem(DEFAULT_MAX_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicket() throws Exception {
        // Initialize the database
        ticketService.save(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).get();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .username(UPDATED_USERNAME)
            .purchased(UPDATED_PURCHASED)
            .reservationId(UPDATED_RESERVATION_ID)
            .seatNumber(UPDATED_SEAT_NUMBER)
            .maxKg(UPDATED_MAX_KG)
            .price(UPDATED_PRICE);

        restTicketMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTicket)))
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testTicket.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTicket.getPurchased()).isEqualTo(UPDATED_PURCHASED);
        assertThat(testTicket.getReservationId()).isEqualTo(UPDATED_RESERVATION_ID);
        assertThat(testTicket.getSeatNumber()).isEqualTo(UPDATED_SEAT_NUMBER);
        assertThat(testTicket.getMaxKg()).isEqualTo(UPDATED_MAX_KG);
        assertThat(testTicket.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Create the Ticket

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTicket() throws Exception {
        // Initialize the database
        ticketService.save(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Delete the ticket
        restTicketMockMvc.perform(delete("/api/tickets/{id}", ticket.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
