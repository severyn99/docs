package com.yankevych.tickets.service;

import com.yankevych.tickets.domain.ServiceUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ServiceUser}.
 */
public interface ServiceUserService {

    /**
     * Save a serviceUser.
     *
     * @param serviceUser the entity to save.
     * @return the persisted entity.
     */
    ServiceUser save(ServiceUser serviceUser);

    /**
     * Get all the serviceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceUser> findAll(Pageable pageable);

    /**
     * Get the "id" serviceUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceUser> findOne(Long id);

    /**
     * Delete the "id" serviceUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
