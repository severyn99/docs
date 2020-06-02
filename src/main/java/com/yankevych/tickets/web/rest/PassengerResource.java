package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.domain.Passenger;
import com.yankevych.tickets.service.PassengerService;
import com.yankevych.tickets.web.rest.errors.BadRequestAlertException;
import com.yankevych.tickets.service.dto.PassengerCriteria;
import com.yankevych.tickets.service.PassengerQueryService;

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
 * REST controller for managing {@link com.yankevych.tickets.domain.Passenger}.
 */
@RestController
@RequestMapping("/api")
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);

    private static final String ENTITY_NAME = "passenger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassengerService passengerService;

    private final PassengerQueryService passengerQueryService;

    public PassengerResource(PassengerService passengerService, PassengerQueryService passengerQueryService) {
        this.passengerService = passengerService;
        this.passengerQueryService = passengerQueryService;
    }

    /**
     * {@code POST  /passengers} : Create a new passenger.
     *
     * @param passenger the passenger to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passenger, or with status {@code 400 (Bad Request)} if the passenger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passengers")
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Passenger result = passengerService.save(passenger);
        return ResponseEntity.created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passengers} : Updates an existing passenger.
     *
     * @param passenger the passenger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passenger,
     * or with status {@code 400 (Bad Request)} if the passenger is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passenger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passengers")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}", passenger);
        if (passenger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Passenger result = passengerService.save(passenger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passenger.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /passengers} : get all the passengers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passengers in body.
     */
    @GetMapping("/passengers")
    public ResponseEntity<List<Passenger>> getAllPassengers(PassengerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Passengers by criteria: {}", criteria);
        Page<Passenger> page = passengerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /passengers/count} : count all the passengers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/passengers/count")
    public ResponseEntity<Long> countPassengers(PassengerCriteria criteria) {
        log.debug("REST request to count Passengers by criteria: {}", criteria);
        return ResponseEntity.ok().body(passengerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /passengers/:id} : get the "id" passenger.
     *
     * @param id the id of the passenger to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passenger, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passengers/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Optional<Passenger> passenger = passengerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passenger);
    }

    /**
     * {@code DELETE  /passengers/:id} : delete the "id" passenger.
     *
     * @param id the id of the passenger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);
        passengerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
