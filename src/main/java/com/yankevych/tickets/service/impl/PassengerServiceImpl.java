package com.yankevych.tickets.service.impl;

import com.yankevych.tickets.service.PassengerService;
import com.yankevych.tickets.domain.Passenger;
import com.yankevych.tickets.repository.PassengerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Passenger}.
 */
@Service
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private final Logger log = LoggerFactory.getLogger(PassengerServiceImpl.class);

    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    /**
     * Save a passenger.
     *
     * @param passenger the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Passenger save(Passenger passenger) {
        log.debug("Request to save Passenger : {}", passenger);
        return passengerRepository.save(passenger);
    }

    /**
     * Get all the passengers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Passenger> findAll(Pageable pageable) {
        log.debug("Request to get all Passengers");
        return passengerRepository.findAll(pageable);
    }

    /**
     * Get one passenger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Passenger> findOne(Long id) {
        log.debug("Request to get Passenger : {}", id);
        return passengerRepository.findById(id);
    }

    /**
     * Delete the passenger by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Passenger : {}", id);
        passengerRepository.deleteById(id);
    }
}
