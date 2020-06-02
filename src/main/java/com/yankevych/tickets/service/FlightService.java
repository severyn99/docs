package com.yankevych.tickets.service;

import com.yankevych.tickets.domain.Flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Flight}.
 */
public interface FlightService {

    /**
     * Save a flight.
     *
     * @param flight the entity to save.
     * @return the persisted entity.
     */
    Flight save(Flight flight);

    /**
     * Get all the flights.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Flight> findAll(Pageable pageable);

    /**
     * Get the "id" flight.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Flight> findOne(Long id);

    /**
     * Delete the "id" flight.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
