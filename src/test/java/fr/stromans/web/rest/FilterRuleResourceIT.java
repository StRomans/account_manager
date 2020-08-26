package fr.stromans.web.rest;

import fr.stromans.AccountManagerApp;
import fr.stromans.domain.FilterRule;
import fr.stromans.domain.ClassificationRule;
import fr.stromans.repository.FilterRuleRepository;

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

import fr.stromans.domain.enumeration.RuleField;
import fr.stromans.domain.enumeration.RuleOperator;
/**
 * Integration tests for the {@link FilterRuleResource} REST controller.
 */
@SpringBootTest(classes = AccountManagerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FilterRuleResourceIT {

    private static final RuleField DEFAULT_FIELD = RuleField.AMOUNT;
    private static final RuleField UPDATED_FIELD = RuleField.DATE;

    private static final RuleOperator DEFAULT_OPERATOR = RuleOperator.EQUALS;
    private static final RuleOperator UPDATED_OPERATOR = RuleOperator.CONTAINS;

    private static final String DEFAULT_STRING_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_VALUE = "BBBBBBBBBB";

    @Autowired
    private FilterRuleRepository filterRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilterRuleMockMvc;

    private FilterRule filterRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilterRule createEntity(EntityManager em) {
        FilterRule filterRule = new FilterRule()
            .field(DEFAULT_FIELD)
            .operator(DEFAULT_OPERATOR)
            .stringValue(DEFAULT_STRING_VALUE);
        // Add required entity
        ClassificationRule classificationRule;
        if (TestUtil.findAll(em, ClassificationRule.class).isEmpty()) {
            classificationRule = ClassificationRuleResourceIT.createEntity(em);
            em.persist(classificationRule);
            em.flush();
        } else {
            classificationRule = TestUtil.findAll(em, ClassificationRule.class).get(0);
        }
        filterRule.setClassificationRule(classificationRule);
        return filterRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilterRule createUpdatedEntity(EntityManager em) {
        FilterRule filterRule = new FilterRule()
            .field(UPDATED_FIELD)
            .operator(UPDATED_OPERATOR)
            .stringValue(UPDATED_STRING_VALUE);
        // Add required entity
        ClassificationRule classificationRule;
        if (TestUtil.findAll(em, ClassificationRule.class).isEmpty()) {
            classificationRule = ClassificationRuleResourceIT.createUpdatedEntity(em);
            em.persist(classificationRule);
            em.flush();
        } else {
            classificationRule = TestUtil.findAll(em, ClassificationRule.class).get(0);
        }
        filterRule.setClassificationRule(classificationRule);
        return filterRule;
    }

    @BeforeEach
    public void initTest() {
        filterRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilterRule() throws Exception {
        int databaseSizeBeforeCreate = filterRuleRepository.findAll().size();
        // Create the FilterRule
        restFilterRuleMockMvc.perform(post("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isCreated());

        // Validate the FilterRule in the database
        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeCreate + 1);
        FilterRule testFilterRule = filterRuleList.get(filterRuleList.size() - 1);
        assertThat(testFilterRule.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testFilterRule.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testFilterRule.getStringValue()).isEqualTo(DEFAULT_STRING_VALUE);
    }

    @Test
    @Transactional
    public void createFilterRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filterRuleRepository.findAll().size();

        // Create the FilterRule with an existing ID
        filterRule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilterRuleMockMvc.perform(post("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isBadRequest());

        // Validate the FilterRule in the database
        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFieldIsRequired() throws Exception {
        int databaseSizeBeforeTest = filterRuleRepository.findAll().size();
        // set the field null
        filterRule.setField(null);

        // Create the FilterRule, which fails.


        restFilterRuleMockMvc.perform(post("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isBadRequest());

        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = filterRuleRepository.findAll().size();
        // set the field null
        filterRule.setOperator(null);

        // Create the FilterRule, which fails.


        restFilterRuleMockMvc.perform(post("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isBadRequest());

        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStringValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = filterRuleRepository.findAll().size();
        // set the field null
        filterRule.setStringValue(null);

        // Create the FilterRule, which fails.


        restFilterRuleMockMvc.perform(post("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isBadRequest());

        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFilterRules() throws Exception {
        // Initialize the database
        filterRuleRepository.saveAndFlush(filterRule);

        // Get all the filterRuleList
        restFilterRuleMockMvc.perform(get("/api/filter-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filterRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].field").value(hasItem(DEFAULT_FIELD.toString())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].stringValue").value(hasItem(DEFAULT_STRING_VALUE)));
    }
    
    @Test
    @Transactional
    public void getFilterRule() throws Exception {
        // Initialize the database
        filterRuleRepository.saveAndFlush(filterRule);

        // Get the filterRule
        restFilterRuleMockMvc.perform(get("/api/filter-rules/{id}", filterRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filterRule.getId().intValue()))
            .andExpect(jsonPath("$.field").value(DEFAULT_FIELD.toString()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.stringValue").value(DEFAULT_STRING_VALUE));
    }
    @Test
    @Transactional
    public void getNonExistingFilterRule() throws Exception {
        // Get the filterRule
        restFilterRuleMockMvc.perform(get("/api/filter-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilterRule() throws Exception {
        // Initialize the database
        filterRuleRepository.saveAndFlush(filterRule);

        int databaseSizeBeforeUpdate = filterRuleRepository.findAll().size();

        // Update the filterRule
        FilterRule updatedFilterRule = filterRuleRepository.findById(filterRule.getId()).get();
        // Disconnect from session so that the updates on updatedFilterRule are not directly saved in db
        em.detach(updatedFilterRule);
        updatedFilterRule
            .field(UPDATED_FIELD)
            .operator(UPDATED_OPERATOR)
            .stringValue(UPDATED_STRING_VALUE);

        restFilterRuleMockMvc.perform(put("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFilterRule)))
            .andExpect(status().isOk());

        // Validate the FilterRule in the database
        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeUpdate);
        FilterRule testFilterRule = filterRuleList.get(filterRuleList.size() - 1);
        assertThat(testFilterRule.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testFilterRule.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testFilterRule.getStringValue()).isEqualTo(UPDATED_STRING_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingFilterRule() throws Exception {
        int databaseSizeBeforeUpdate = filterRuleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilterRuleMockMvc.perform(put("/api/filter-rules").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filterRule)))
            .andExpect(status().isBadRequest());

        // Validate the FilterRule in the database
        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFilterRule() throws Exception {
        // Initialize the database
        filterRuleRepository.saveAndFlush(filterRule);

        int databaseSizeBeforeDelete = filterRuleRepository.findAll().size();

        // Delete the filterRule
        restFilterRuleMockMvc.perform(delete("/api/filter-rules/{id}", filterRule.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FilterRule> filterRuleList = filterRuleRepository.findAll();
        assertThat(filterRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
