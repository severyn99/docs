package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.domain.Flight;
import com.yankevych.tickets.service.FlightService;
import com.yankevych.tickets.web.rest.errors.BadRequestAlertException;
import com.yankevych.tickets.service.dto.FlightCriteria;
import com.yankevych.tickets.service.FlightQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.yankevych.tickets.domain.Flight}.
 */
@RestController
@RequestMapping("/api")
public class FlightResource {

    private final Logger log = LoggerFactory.getLogger(FlightResource.class);

    private static final String ENTITY_NAME = "flight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlightService flightService;

    private final FlightQueryService flightQueryService;

    public FlightResource(FlightService flightService, FlightQueryService flightQueryService) {
        this.flightService = flightService;
        this.flightQueryService = flightQueryService;
    }

    /**
     * {@code POST  /flights} : Create a new flight.
     *
     * @param flight the flight to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flight, or with status {@code 400 (Bad Request)} if the flight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) throws URISyntaxException {
        log.debug("REST request to save Flight : {}", flight);
        if (flight.getId() != null) {
            throw new BadRequestAlertException("A new flight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Flight result = flightService.save(flight);
        return ResponseEntity.created(new URI("/api/flights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flights} : Updates an existing flight.
     *
     * @param flight the flight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flight,
     * or with status {@code 400 (Bad Request)} if the flight is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flights")
    public ResponseEntity<Flight> updateFlight(@RequestBody Flight flight) throws URISyntaxException {
        log.debug("REST request to update Flight : {}", flight);
        if (flight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Flight result = flightService.save(flight);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flight.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flights} : get all the flights.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flights in body.
     */
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights(FlightCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Flights by criteria: {}", criteria);
        Page<Flight> page = flightQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flights/count} : count all the flights.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flights/count")
    public ResponseEntity<Long> countFlights(FlightCriteria criteria) {
        log.debug("REST request to count Flights by criteria: {}", criteria);
        return ResponseEntity.ok().body(flightQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flights/:id} : get the "id" flight.
     *
     * @param id the id of the flight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flight, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        log.debug("REST request to get Flight : {}", id);
        Optional<Flight> flight = flightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flight);
    }

    /**
     * {@code DELETE  /flights/:id} : delete the "id" flight.
     *
     * @param id the id of the flight to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        log.debug("REST request to delete Flight : {}", id);
        flightService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
