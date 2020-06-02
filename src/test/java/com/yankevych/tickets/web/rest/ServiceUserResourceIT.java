package com.yankevych.tickets.web.rest;

import com.yankevych.tickets.TicketsApp;
import com.yankevych.tickets.domain.ServiceUser;
import com.yankevych.tickets.domain.Ticket;
import com.yankevych.tickets.domain.Payment;
import com.yankevych.tickets.repository.ServiceUserRepository;
import com.yankevych.tickets.service.ServiceUserService;
import com.yankevych.tickets.service.dto.ServiceUserCriteria;
import com.yankevych.tickets.service.ServiceUserQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ServiceUserResource} REST controller.
 */
@SpringBootTest(classes = TicketsApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ServiceUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_CARD = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_CARD = "BBBBBBBBBB";

    @Autowired
    private ServiceUserRepository serviceUserRepository;

    @Autowired
    private ServiceUserService serviceUserService;

    @Autowired
    private ServiceUserQueryService serviceUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceUserMockMvc;

    private ServiceUser serviceUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceUser createEntity(EntityManager em) {
        ServiceUser serviceUser = new ServiceUser()
            .username(DEFAULT_USERNAME)
            .age(DEFAULT_AGE)
            .phone(DEFAULT_PHONE)
            .creditCard(DEFAULT_CREDIT_CARD);
        return serviceUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceUser createUpdatedEntity(EntityManager em) {
        ServiceUser serviceUser = new ServiceUser()
            .username(UPDATED_USERNAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .creditCard(UPDATED_CREDIT_CARD);
        return serviceUser;
    }

    @BeforeEach
    public void initTest() {
        serviceUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceUser() throws Exception {
        int databaseSizeBeforeCreate = serviceUserRepository.findAll().size();

        // Create the ServiceUser
        restServiceUserMockMvc.perform(post("/api/service-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceUser)))
            .andExpect(status().isCreated());

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testServiceUser.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testServiceUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testServiceUser.getCreditCard()).isEqualTo(DEFAULT_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void createServiceUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceUserRepository.findAll().size();

        // Create the ServiceUser with an existing ID
        serviceUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceUserMockMvc.perform(post("/api/service-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceUser)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceUserRepository.findAll().size();
        // set the field null
        serviceUser.setUsername(null);

        // Create the ServiceUser, which fails.

        restServiceUserMockMvc.perform(post("/api/service-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceUser)))
            .andExpect(status().isBadRequest());

        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceUsers() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList
        restServiceUserMockMvc.perform(get("/api/service-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].creditCard").value(hasItem(DEFAULT_CREDIT_CARD)));
    }
    
    @Test
    @Transactional
    public void getServiceUser() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get the serviceUser
        restServiceUserMockMvc.perform(get("/api/service-users/{id}", serviceUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.creditCard").value(DEFAULT_CREDIT_CARD));
    }


    @Test
    @Transactional
    public void getServiceUsersByIdFiltering() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        Long id = serviceUser.getId();

        defaultServiceUserShouldBeFound("id.equals=" + id);
        defaultServiceUserShouldNotBeFound("id.notEquals=" + id);

        defaultServiceUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServiceUserShouldNotBeFound("id.greaterThan=" + id);

        defaultServiceUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServiceUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllServiceUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username equals to DEFAULT_USERNAME
        defaultServiceUserShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the serviceUserList where username equals to UPDATED_USERNAME
        defaultServiceUserShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username not equals to DEFAULT_USERNAME
        defaultServiceUserShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the serviceUserList where username not equals to UPDATED_USERNAME
        defaultServiceUserShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultServiceUserShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the serviceUserList where username equals to UPDATED_USERNAME
        defaultServiceUserShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username is not null
        defaultServiceUserShouldBeFound("username.specified=true");

        // Get all the serviceUserList where username is null
        defaultServiceUserShouldNotBeFound("username.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username contains DEFAULT_USERNAME
        defaultServiceUserShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the serviceUserList where username contains UPDATED_USERNAME
        defaultServiceUserShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where username does not contain DEFAULT_USERNAME
        defaultServiceUserShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the serviceUserList where username does not contain UPDATED_USERNAME
        defaultServiceUserShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }


    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age equals to DEFAULT_AGE
        defaultServiceUserShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the serviceUserList where age equals to UPDATED_AGE
        defaultServiceUserShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age not equals to DEFAULT_AGE
        defaultServiceUserShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the serviceUserList where age not equals to UPDATED_AGE
        defaultServiceUserShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age in DEFAULT_AGE or UPDATED_AGE
        defaultServiceUserShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the serviceUserList where age equals to UPDATED_AGE
        defaultServiceUserShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age is not null
        defaultServiceUserShouldBeFound("age.specified=true");

        // Get all the serviceUserList where age is null
        defaultServiceUserShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age is greater than or equal to DEFAULT_AGE
        defaultServiceUserShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the serviceUserList where age is greater than or equal to UPDATED_AGE
        defaultServiceUserShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age is less than or equal to DEFAULT_AGE
        defaultServiceUserShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the serviceUserList where age is less than or equal to SMALLER_AGE
        defaultServiceUserShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age is less than DEFAULT_AGE
        defaultServiceUserShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the serviceUserList where age is less than UPDATED_AGE
        defaultServiceUserShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where age is greater than DEFAULT_AGE
        defaultServiceUserShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the serviceUserList where age is greater than SMALLER_AGE
        defaultServiceUserShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }


    @Test
    @Transactional
    public void getAllServiceUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone equals to DEFAULT_PHONE
        defaultServiceUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the serviceUserList where phone equals to UPDATED_PHONE
        defaultServiceUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone not equals to DEFAULT_PHONE
        defaultServiceUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the serviceUserList where phone not equals to UPDATED_PHONE
        defaultServiceUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultServiceUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the serviceUserList where phone equals to UPDATED_PHONE
        defaultServiceUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone is not null
        defaultServiceUserShouldBeFound("phone.specified=true");

        // Get all the serviceUserList where phone is null
        defaultServiceUserShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone contains DEFAULT_PHONE
        defaultServiceUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the serviceUserList where phone contains UPDATED_PHONE
        defaultServiceUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where phone does not contain DEFAULT_PHONE
        defaultServiceUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the serviceUserList where phone does not contain UPDATED_PHONE
        defaultServiceUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllServiceUsersByCreditCardIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard equals to DEFAULT_CREDIT_CARD
        defaultServiceUserShouldBeFound("creditCard.equals=" + DEFAULT_CREDIT_CARD);

        // Get all the serviceUserList where creditCard equals to UPDATED_CREDIT_CARD
        defaultServiceUserShouldNotBeFound("creditCard.equals=" + UPDATED_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByCreditCardIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard not equals to DEFAULT_CREDIT_CARD
        defaultServiceUserShouldNotBeFound("creditCard.notEquals=" + DEFAULT_CREDIT_CARD);

        // Get all the serviceUserList where creditCard not equals to UPDATED_CREDIT_CARD
        defaultServiceUserShouldBeFound("creditCard.notEquals=" + UPDATED_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByCreditCardIsInShouldWork() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard in DEFAULT_CREDIT_CARD or UPDATED_CREDIT_CARD
        defaultServiceUserShouldBeFound("creditCard.in=" + DEFAULT_CREDIT_CARD + "," + UPDATED_CREDIT_CARD);

        // Get all the serviceUserList where creditCard equals to UPDATED_CREDIT_CARD
        defaultServiceUserShouldNotBeFound("creditCard.in=" + UPDATED_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByCreditCardIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard is not null
        defaultServiceUserShouldBeFound("creditCard.specified=true");

        // Get all the serviceUserList where creditCard is null
        defaultServiceUserShouldNotBeFound("creditCard.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceUsersByCreditCardContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard contains DEFAULT_CREDIT_CARD
        defaultServiceUserShouldBeFound("creditCard.contains=" + DEFAULT_CREDIT_CARD);

        // Get all the serviceUserList where creditCard contains UPDATED_CREDIT_CARD
        defaultServiceUserShouldNotBeFound("creditCard.contains=" + UPDATED_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void getAllServiceUsersByCreditCardNotContainsSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);

        // Get all the serviceUserList where creditCard does not contain DEFAULT_CREDIT_CARD
        defaultServiceUserShouldNotBeFound("creditCard.doesNotContain=" + DEFAULT_CREDIT_CARD);

        // Get all the serviceUserList where creditCard does not contain UPDATED_CREDIT_CARD
        defaultServiceUserShouldBeFound("creditCard.doesNotContain=" + UPDATED_CREDIT_CARD);
    }


    @Test
    @Transactional
    public void getAllServiceUsersByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);
        Ticket tickets = TicketResourceIT.createEntity(em);
        em.persist(tickets);
        em.flush();
        serviceUser.addTickets(tickets);
        serviceUserRepository.saveAndFlush(serviceUser);
        Long ticketsId = tickets.getId();

        // Get all the serviceUserList where tickets equals to ticketsId
        defaultServiceUserShouldBeFound("ticketsId.equals=" + ticketsId);

        // Get all the serviceUserList where tickets equals to ticketsId + 1
        defaultServiceUserShouldNotBeFound("ticketsId.equals=" + (ticketsId + 1));
    }


    @Test
    @Transactional
    public void getAllServiceUsersByPaymentsIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceUserRepository.saveAndFlush(serviceUser);
        Payment payments = PaymentResourceIT.createEntity(em);
        em.persist(payments);
        em.flush();
        serviceUser.addPayments(payments);
        serviceUserRepository.saveAndFlush(serviceUser);
        Long paymentsId = payments.getId();

        // Get all the serviceUserList where payments equals to paymentsId
        defaultServiceUserShouldBeFound("paymentsId.equals=" + paymentsId);

        // Get all the serviceUserList where payments equals to paymentsId + 1
        defaultServiceUserShouldNotBeFound("paymentsId.equals=" + (paymentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceUserShouldBeFound(String filter) throws Exception {
        restServiceUserMockMvc.perform(get("/api/service-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].creditCard").value(hasItem(DEFAULT_CREDIT_CARD)));

        // Check, that the count call also returns 1
        restServiceUserMockMvc.perform(get("/api/service-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceUserShouldNotBeFound(String filter) throws Exception {
        restServiceUserMockMvc.perform(get("/api/service-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceUserMockMvc.perform(get("/api/service-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServiceUser() throws Exception {
        // Get the serviceUser
        restServiceUserMockMvc.perform(get("/api/service-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceUser() throws Exception {
        // Initialize the database
        serviceUserService.save(serviceUser);

        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().size();

        // Update the serviceUser
        ServiceUser updatedServiceUser = serviceUserRepository.findById(serviceUser.getId()).get();
        // Disconnect from session so that the updates on updatedServiceUser are not directly saved in db
        em.detach(updatedServiceUser);
        updatedServiceUser
            .username(UPDATED_USERNAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .creditCard(UPDATED_CREDIT_CARD);

        restServiceUserMockMvc.perform(put("/api/service-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceUser)))
            .andExpect(status().isOk());

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testServiceUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testServiceUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testServiceUser.getCreditCard()).isEqualTo(UPDATED_CREDIT_CARD);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().size();

        // Create the ServiceUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceUserMockMvc.perform(put("/api/service-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceUser)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceUser() throws Exception {
        // Initialize the database
        serviceUserService.save(serviceUser);

        int databaseSizeBeforeDelete = serviceUserRepository.findAll().size();

        // Delete the serviceUser
        restServiceUserMockMvc.perform(delete("/api/service-users/{id}", serviceUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
