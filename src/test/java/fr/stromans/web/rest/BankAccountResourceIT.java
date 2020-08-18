package fr.stromans.web.rest;

import fr.stromans.AccountManagerApp;
import fr.stromans.domain.BankAccount;
import fr.stromans.domain.Currency;
import fr.stromans.domain.User;
import fr.stromans.repository.BankAccountRepository;
import fr.stromans.service.BankAccountService;
import fr.stromans.service.dto.BankAccountCriteria;
import fr.stromans.service.BankAccountQueryService;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@SpringBootTest(classes = AccountManagerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BankAccountResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountQueryService bankAccountQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .label(DEFAULT_LABEL);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        bankAccount.setCurrency(currency);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bankAccount.setOwner(user);
        return bankAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .label(UPDATED_LABEL);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createUpdatedEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        bankAccount.setCurrency(currency);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bankAccount.setOwner(user);
        return bankAccount;
    }

    @BeforeEach
    public void initTest() {
        bankAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();
        // Create the BankAccount
        restBankAccountMockMvc.perform(post("/api/bank-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bankAccount)))
            .andExpect(status().isCreated());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createBankAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankAccountMockMvc.perform(post("/api/bank-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setLabel(null);

        // Create the BankAccount, which fails.


        restBankAccountMockMvc.perform(post("/api/bank-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getBankAccountsByIdFiltering() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        Long id = bankAccount.getId();

        defaultBankAccountShouldBeFound("id.equals=" + id);
        defaultBankAccountShouldNotBeFound("id.notEquals=" + id);

        defaultBankAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultBankAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBankAccountsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label equals to DEFAULT_LABEL
        defaultBankAccountShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the bankAccountList where label equals to UPDATED_LABEL
        defaultBankAccountShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label not equals to DEFAULT_LABEL
        defaultBankAccountShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the bankAccountList where label not equals to UPDATED_LABEL
        defaultBankAccountShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultBankAccountShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the bankAccountList where label equals to UPDATED_LABEL
        defaultBankAccountShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label is not null
        defaultBankAccountShouldBeFound("label.specified=true");

        // Get all the bankAccountList where label is null
        defaultBankAccountShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllBankAccountsByLabelContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label contains DEFAULT_LABEL
        defaultBankAccountShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the bankAccountList where label contains UPDATED_LABEL
        defaultBankAccountShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where label does not contain DEFAULT_LABEL
        defaultBankAccountShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the bankAccountList where label does not contain UPDATED_LABEL
        defaultBankAccountShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllBankAccountsByCurrencyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Currency currency = bankAccount.getCurrency();
        bankAccountRepository.saveAndFlush(bankAccount);
        Long currencyId = currency.getId();

        // Get all the bankAccountList where currency equals to currencyId
        defaultBankAccountShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the bankAccountList where currency equals to currencyId + 1
        defaultBankAccountShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }


    @Test
    @Transactional
    public void getAllBankAccountsByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = bankAccount.getOwner();
        bankAccountRepository.saveAndFlush(bankAccount);
        Long ownerId = owner.getId();

        // Get all the bankAccountList where owner equals to ownerId
        defaultBankAccountShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the bankAccountList where owner equals to ownerId + 1
        defaultBankAccountShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankAccountShouldBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankAccountShouldNotBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBankAccount() throws Exception {
        // Initialize the database
        bankAccountService.save(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).get();
        // Disconnect from session so that the updates on updatedBankAccount are not directly saved in db
        em.detach(updatedBankAccount);
        updatedBankAccount
            .label(UPDATED_LABEL);

        restBankAccountMockMvc.perform(put("/api/bank-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBankAccount)))
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc.perform(put("/api/bank-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBankAccount() throws Exception {
        // Initialize the database
        bankAccountService.save(bankAccount);

        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Delete the bankAccount
        restBankAccountMockMvc.perform(delete("/api/bank-accounts/{id}", bankAccount.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
