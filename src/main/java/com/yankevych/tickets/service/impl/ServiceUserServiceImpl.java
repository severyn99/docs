package com.yankevych.tickets.service.impl;

import com.yankevych.tickets.service.ServiceUserService;
import com.yankevych.tickets.domain.ServiceUser;
import com.yankevych.tickets.repository.ServiceUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ServiceUser}.
 */
@Service
@Transactional
public class ServiceUserServiceImpl implements ServiceUserService {

    private final Logger log = LoggerFactory.getLogger(ServiceUserServiceImpl.class);

    private final ServiceUserRepository serviceUserRepository;

    public ServiceUserServiceImpl(ServiceUserRepository serviceUserRepository) {
        this.serviceUserRepository = serviceUserRepository;
    }

    /**
     * Save a serviceUser.
     *
     * @param serviceUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceUser save(ServiceUser serviceUser) {
        log.debug("Request to save ServiceUser : {}", serviceUser);
        return serviceUserRepository.save(serviceUser);
    }

    /**
     * Get all the serviceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceUser> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceUsers");
        return serviceUserRepository.findAll(pageable);
    }

    /**
     * Get one serviceUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceUser> findOne(Long id) {
        log.debug("Request to get ServiceUser : {}", id);
        return serviceUserRepository.findById(id);
    }

    /**
     * Delete the serviceUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceUser : {}", id);
        serviceUserRepository.deleteById(id);
    }
}
