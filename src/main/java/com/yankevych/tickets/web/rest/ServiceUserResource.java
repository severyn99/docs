package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.domain.ServiceUser;
import com.yankevych.tickets.service.ServiceUserService;
import com.yankevych.tickets.web.rest.errors.BadRequestAlertException;
import com.yankevych.tickets.service.dto.ServiceUserCriteria;
import com.yankevych.tickets.service.ServiceUserQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.yankevych.tickets.domain.ServiceUser}.
 */
@RestController
@RequestMapping("/api")
public class ServiceUserResource {

    private final Logger log = LoggerFactory.getLogger(ServiceUserResource.class);

    private static final String ENTITY_NAME = "serviceUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceUserService serviceUserService;

    private final ServiceUserQueryService serviceUserQueryService;

    public ServiceUserResource(ServiceUserService serviceUserService, ServiceUserQueryService serviceUserQueryService) {
        this.serviceUserService = serviceUserService;
        this.serviceUserQueryService = serviceUserQueryService;
    }

    /**
     * {@code POST  /service-users} : Create a new serviceUser.
     *
     * @param serviceUser the serviceUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceUser, or with status {@code 400 (Bad Request)} if the serviceUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-users")
    public ResponseEntity<ServiceUser> createServiceUser(@Valid @RequestBody ServiceUser serviceUser) throws URISyntaxException {
        log.debug("REST request to save ServiceUser : {}", serviceUser);
        if (serviceUser.getId() != null) {
            throw new BadRequestAlertException("A new serviceUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceUser result = serviceUserService.save(serviceUser);
        return ResponseEntity.created(new URI("/api/service-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-users} : Updates an existing serviceUser.
     *
     * @param serviceUser the serviceUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceUser,
     * or with status {@code 400 (Bad Request)} if the serviceUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-users")
    public ResponseEntity<ServiceUser> updateServiceUser(@Valid @RequestBody ServiceUser serviceUser) throws URISyntaxException {
        log.debug("REST request to update ServiceUser : {}", serviceUser);
        if (serviceUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceUser result = serviceUserService.save(serviceUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-users} : get all the serviceUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceUsers in body.
     */
    @GetMapping("/service-users")
    public ResponseEntity<List<ServiceUser>> getAllServiceUsers(ServiceUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ServiceUsers by criteria: {}", criteria);
        Page<ServiceUser> page = serviceUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-users/count} : count all the serviceUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/service-users/count")
    public ResponseEntity<Long> countServiceUsers(ServiceUserCriteria criteria) {
        log.debug("REST request to count ServiceUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-users/:id} : get the "id" serviceUser.
     *
     * @param id the id of the serviceUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-users/{id}")
    public ResponseEntity<ServiceUser> getServiceUser(@PathVariable Long id) {
        log.debug("REST request to get ServiceUser : {}", id);
        Optional<ServiceUser> serviceUser = serviceUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceUser);
    }

    /**
     * {@code DELETE  /service-users/:id} : delete the "id" serviceUser.
     *
     * @param id the id of the serviceUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-users/{id}")
    public ResponseEntity<Void> deleteServiceUser(@PathVariable Long id) {
        log.debug("REST request to delete ServiceUser : {}", id);
        serviceUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
