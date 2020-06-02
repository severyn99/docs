package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.domain.Reservation;
import com.yankevych.tickets.service.ReservationService;
import com.yankevych.tickets.web.rest.errors.BadRequestAlertException;
import com.yankevych.tickets.service.dto.ReservationCriteria;
import com.yankevych.tickets.service.ReservationQueryService;

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
 * REST controller for managing {@link com.yankevych.tickets.domain.Reservation}.
 */
@RestController
@RequestMapping("/api")
public class ReservationResource {

    private final Logger log = LoggerFactory.getLogger(ReservationResource.class);

    private static final String ENTITY_NAME = "reservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationService reservationService;

    private final ReservationQueryService reservationQueryService;

    public ReservationResource(ReservationService reservationService, ReservationQueryService reservationQueryService) {
        this.reservationService = reservationService;
        this.reservationQueryService = reservationQueryService;
    }

    /**
     * {@code POST  /reservations} : Create a new reservation.
     *
     * @param reservation the reservation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservation, or with status {@code 400 (Bad Request)} if the reservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) throws URISyntaxException {
        log.debug("REST request to save Reservation : {}", reservation);
        if (reservation.getId() != null) {
            throw new BadRequestAlertException("A new reservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reservation result = reservationService.save(reservation);
        return ResponseEntity.created(new URI("/api/reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reservations} : Updates an existing reservation.
     *
     * @param reservation the reservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reservations")
    public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation reservation) throws URISyntaxException {
        log.debug("REST request to update Reservation : {}", reservation);
        if (reservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Reservation result = reservationService.save(reservation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reservations} : get all the reservations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(ReservationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Reservations by criteria: {}", criteria);
        Page<Reservation> page = reservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reservations/count} : count all the reservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reservations/count")
    public ResponseEntity<Long> countReservations(ReservationCriteria criteria) {
        log.debug("REST request to count Reservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(reservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reservations/:id} : get the "id" reservation.
     *
     * @param id the id of the reservation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        log.debug("REST request to get Reservation : {}", id);
        Optional<Reservation> reservation = reservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservation);
    }

    /**
     * {@code DELETE  /reservations/:id} : delete the "id" reservation.
     *
     * @param id the id of the reservation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        log.debug("REST request to delete Reservation : {}", id);
        reservationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
