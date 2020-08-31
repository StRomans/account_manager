package fr.stromans.web.rest;

import fr.stromans.AccountManagerApp;
import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.User;
import fr.stromans.domain.BankAccount;
import fr.stromans.domain.Transaction;
import fr.stromans.domain.FilterRule;
import fr.stromans.domain.SubCategory;
import fr.stromans.repository.ClassificationRuleRepository;
import fr.stromans.service.ClassificationRuleService;
import fr.stromans.service.dto.ClassificationRuleCriteria;
import fr.stromans.service.ClassificationRuleQueryService;

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
 * Integration tests for the {@link ClassificationRuleResource} REST controller.
 */
@SpringBootTest(classes = AccountManagerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClassificationRuleResourceIT {

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    @Autowired
    private ClassificationRuleRepository classificationRuleRepository;

    @Autowired
    private ClassificationRuleService classificationRuleService;

    @Autowired
    private ClassificationRuleQueryService classificationRuleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassificationRuleMockMvc;

    private ClassificationRule classificationRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationRule createEntity(EntityManager em) {
        ClassificationRule classificationRule = new ClassificationRule()
            .priority(DEFAULT_PRIORITY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        classificationRule.setOwner(user);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createEntity(em);
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        classificationRule.setBankAccount(bankAccount);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        classificationRule.setSubCategory(subCategory);
        return classificationRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationRule createUpdatedEntity(EntityManager em) {
        ClassificationRule classificationRule = new ClassificationRule()
            .priority(UPDATED_PRIORITY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        classificationRule.setOwner(user);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createUpdatedEntity(em);
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        classificationRule.setBankAccount(bankAccount);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createUpdatedEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        classificationRule.setSubCategory(subCategory);
        return classificationRule;
    }

    @BeforeEach
    public void initTest() {
        classificationRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassificationRule() throws Exception {
        int databaseSizeBeforeCreate = classificationRuleRepository.findAll().size();
        // Create the ClassificationRule
        restClassificationRuleMockMvc.perform(post("/api/classification-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationRule)))
            .andExpect(status().isCreated());

        // Validate the ClassificationRule in the database
        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeCreate + 1);
        ClassificationRule testClassificationRule = classificationRuleList.get(classificationRuleList.size() - 1);
        assertThat(testClassificationRule.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    public void createClassificationRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classificationRuleRepository.findAll().size();

        // Create the ClassificationRule with an existing ID
        classificationRule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationRuleMockMvc.perform(post("/api/classification-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationRule)))
            .andExpect(status().isBadRequest());

        // Validate the ClassificationRule in the database
        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRuleRepository.findAll().size();
        // set the field null
        classificationRule.setPriority(null);

        // Create the ClassificationRule, which fails.


        restClassificationRuleMockMvc.perform(post("/api/classification-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationRule)))
            .andExpect(status().isBadRequest());

        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassificationRules() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList
        restClassificationRuleMockMvc.perform(get("/api/classification-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }
    
    @Test
    @Transactional
    public void getClassificationRule() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get the classificationRule
        restClassificationRuleMockMvc.perform(get("/api/classification-rules/{id}", classificationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classificationRule.getId().intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }


    @Test
    @Transactional
    public void getClassificationRulesByIdFiltering() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        Long id = classificationRule.getId();

        defaultClassificationRuleShouldBeFound("id.equals=" + id);
        defaultClassificationRuleShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationRuleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority equals to DEFAULT_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority equals to UPDATED_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority not equals to DEFAULT_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority not equals to UPDATED_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the classificationRuleList where priority equals to UPDATED_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority is not null
        defaultClassificationRuleShouldBeFound("priority.specified=true");

        // Get all the classificationRuleList where priority is null
        defaultClassificationRuleShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority is greater than or equal to DEFAULT_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority is greater than or equal to (DEFAULT_PRIORITY + 1)
        defaultClassificationRuleShouldNotBeFound("priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1));
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority is less than or equal to DEFAULT_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.lessThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority is less than or equal to SMALLER_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority is less than DEFAULT_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.lessThan=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority is less than (DEFAULT_PRIORITY + 1)
        defaultClassificationRuleShouldBeFound("priority.lessThan=" + (DEFAULT_PRIORITY + 1));
    }

    @Test
    @Transactional
    public void getAllClassificationRulesByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);

        // Get all the classificationRuleList where priority is greater than DEFAULT_PRIORITY
        defaultClassificationRuleShouldNotBeFound("priority.greaterThan=" + DEFAULT_PRIORITY);

        // Get all the classificationRuleList where priority is greater than SMALLER_PRIORITY
        defaultClassificationRuleShouldBeFound("priority.greaterThan=" + SMALLER_PRIORITY);
    }


    @Test
    @Transactional
    public void getAllClassificationRulesByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = classificationRule.getOwner();
        classificationRuleRepository.saveAndFlush(classificationRule);
        Long ownerId = owner.getId();

        // Get all the classificationRuleList where owner equals to ownerId
        defaultClassificationRuleShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the classificationRuleList where owner equals to ownerId + 1
        defaultClassificationRuleShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationRulesByBankAccountIsEqualToSomething() throws Exception {
        // Get already existing entity
        BankAccount bankAccount = classificationRule.getBankAccount();
        classificationRuleRepository.saveAndFlush(classificationRule);
        Long bankAccountId = bankAccount.getId();

        // Get all the classificationRuleList where bankAccount equals to bankAccountId
        defaultClassificationRuleShouldBeFound("bankAccountId.equals=" + bankAccountId);

        // Get all the classificationRuleList where bankAccount equals to bankAccountId + 1
        defaultClassificationRuleShouldNotBeFound("bankAccountId.equals=" + (bankAccountId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationRulesByTransactionsIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);
        Transaction transactions = TransactionResourceIT.createEntity(em);
        em.persist(transactions);
        em.flush();
        classificationRule.addTransactions(transactions);
        classificationRuleRepository.saveAndFlush(classificationRule);
        Long transactionsId = transactions.getId();

        // Get all the classificationRuleList where transactions equals to transactionsId
        defaultClassificationRuleShouldBeFound("transactionsId.equals=" + transactionsId);

        // Get all the classificationRuleList where transactions equals to transactionsId + 1
        defaultClassificationRuleShouldNotBeFound("transactionsId.equals=" + (transactionsId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationRulesByFilterRulesIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRuleRepository.saveAndFlush(classificationRule);
        FilterRule filterRules = FilterRuleResourceIT.createEntity(em);
        em.persist(filterRules);
        em.flush();
        classificationRule.addFilterRules(filterRules);
        classificationRuleRepository.saveAndFlush(classificationRule);
        Long filterRulesId = filterRules.getId();

        // Get all the classificationRuleList where filterRules equals to filterRulesId
        defaultClassificationRuleShouldBeFound("filterRulesId.equals=" + filterRulesId);

        // Get all the classificationRuleList where filterRules equals to filterRulesId + 1
        defaultClassificationRuleShouldNotBeFound("filterRulesId.equals=" + (filterRulesId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationRulesBySubCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        SubCategory subCategory = classificationRule.getSubCategory();
        classificationRuleRepository.saveAndFlush(classificationRule);
        Long subCategoryId = subCategory.getId();

        // Get all the classificationRuleList where subCategory equals to subCategoryId
        defaultClassificationRuleShouldBeFound("subCategoryId.equals=" + subCategoryId);

        // Get all the classificationRuleList where subCategory equals to subCategoryId + 1
        defaultClassificationRuleShouldNotBeFound("subCategoryId.equals=" + (subCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationRuleShouldBeFound(String filter) throws Exception {
        restClassificationRuleMockMvc.perform(get("/api/classification-rules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));

        // Check, that the count call also returns 1
        restClassificationRuleMockMvc.perform(get("/api/classification-rules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationRuleShouldNotBeFound(String filter) throws Exception {
        restClassificationRuleMockMvc.perform(get("/api/classification-rules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationRuleMockMvc.perform(get("/api/classification-rules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClassificationRule() throws Exception {
        // Get the classificationRule
        restClassificationRuleMockMvc.perform(get("/api/classification-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassificationRule() throws Exception {
        // Initialize the database
        classificationRuleService.save(classificationRule);

        int databaseSizeBeforeUpdate = classificationRuleRepository.findAll().size();

        // Update the classificationRule
        ClassificationRule updatedClassificationRule = classificationRuleRepository.findById(classificationRule.getId()).get();
        // Disconnect from session so that the updates on updatedClassificationRule are not directly saved in db
        em.detach(updatedClassificationRule);
        updatedClassificationRule
            .priority(UPDATED_PRIORITY);

        restClassificationRuleMockMvc.perform(put("/api/classification-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassificationRule)))
            .andExpect(status().isOk());

        // Validate the ClassificationRule in the database
        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeUpdate);
        ClassificationRule testClassificationRule = classificationRuleList.get(classificationRuleList.size() - 1);
        assertThat(testClassificationRule.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void updateNonExistingClassificationRule() throws Exception {
        int databaseSizeBeforeUpdate = classificationRuleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationRuleMockMvc.perform(put("/api/classification-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationRule)))
            .andExpect(status().isBadRequest());

        // Validate the ClassificationRule in the database
        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassificationRule() throws Exception {
        // Initialize the database
        classificationRuleService.save(classificationRule);

        int databaseSizeBeforeDelete = classificationRuleRepository.findAll().size();

        // Delete the classificationRule
        restClassificationRuleMockMvc.perform(delete("/api/classification-rules/{id}", classificationRule.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassificationRule> classificationRuleList = classificationRuleRepository.findAll();
        assertThat(classificationRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
