package com.yankevych.tickets.service.impl;

import com.yankevych.tickets.service.FlightService;
import com.yankevych.tickets.domain.Flight;
import com.yankevych.tickets.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Flight}.
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private final Logger log = LoggerFactory.getLogger(FlightServiceImpl.class);

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Save a flight.
     *
     * @param flight the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Flight save(Flight flight) {
        log.debug("Request to save Flight : {}", flight);
        return flightRepository.save(flight);
    }

    /**
     * Get all the flights.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Flight> findAll(Pageable pageable) {
        log.debug("Request to get all Flights");
        return flightRepository.findAll(pageable);
    }

    /**
     * Get one flight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> findOne(Long id) {
        log.debug("Request to get Flight : {}", id);
        return flightRepository.findById(id);
    }

    /**
     * Delete the flight by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Flight : {}", id);
        flightRepository.deleteById(id);
    }
}
