package fr.stromans.web.rest;

import fr.stromans.AccountManagerApp;
import fr.stromans.domain.SubCategory;
import fr.stromans.domain.Category;
import fr.stromans.repository.SubCategoryRepository;
import fr.stromans.service.SubCategoryService;
import fr.stromans.service.dto.SubCategoryCriteria;
import fr.stromans.service.SubCategoryQueryService;

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
 * Integration tests for the {@link SubCategoryResource} REST controller.
 */
@SpringBootTest(classes = AccountManagerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubCategoryResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryQueryService subCategoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory()
            .label(DEFAULT_LABEL);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        subCategory.setCategory(category);
        return subCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createUpdatedEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory()
            .label(UPDATED_LABEL);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        subCategory.setCategory(category);
        return subCategory;
    }

    @BeforeEach
    public void initTest() {
        subCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubCategory() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();
        // Create the SubCategory
        restSubCategoryMockMvc.perform(post("/api/sub-categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategory)))
            .andExpect(status().isCreated());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createSubCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();

        // Create the SubCategory with an existing ID
        subCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoryMockMvc.perform(post("/api/sub-categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategory)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoryRepository.findAll().size();
        // set the field null
        subCategory.setLabel(null);

        // Create the SubCategory, which fails.


        restSubCategoryMockMvc.perform(post("/api/sub-categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategory)))
            .andExpect(status().isBadRequest());

        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubCategories() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        restSubCategoryMockMvc.perform(get("/api/sub-categories/{id}", subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategory.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getSubCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        Long id = subCategory.getId();

        defaultSubCategoryShouldBeFound("id.equals=" + id);
        defaultSubCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultSubCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultSubCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label equals to DEFAULT_LABEL
        defaultSubCategoryShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the subCategoryList where label equals to UPDATED_LABEL
        defaultSubCategoryShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label not equals to DEFAULT_LABEL
        defaultSubCategoryShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the subCategoryList where label not equals to UPDATED_LABEL
        defaultSubCategoryShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultSubCategoryShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the subCategoryList where label equals to UPDATED_LABEL
        defaultSubCategoryShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label is not null
        defaultSubCategoryShouldBeFound("label.specified=true");

        // Get all the subCategoryList where label is null
        defaultSubCategoryShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubCategoriesByLabelContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label contains DEFAULT_LABEL
        defaultSubCategoryShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the subCategoryList where label contains UPDATED_LABEL
        defaultSubCategoryShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where label does not contain DEFAULT_LABEL
        defaultSubCategoryShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the subCategoryList where label does not contain UPDATED_LABEL
        defaultSubCategoryShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        Category category = subCategory.getCategory();
        subCategoryRepository.saveAndFlush(subCategory);
        Long categoryId = category.getId();

        // Get all the subCategoryList where category equals to categoryId
        defaultSubCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the subCategoryList where category equals to categoryId + 1
        defaultSubCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubCategoryShouldBeFound(String filter) throws Exception {
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restSubCategoryMockMvc.perform(get("/api/sub-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubCategoryShouldNotBeFound(String filter) throws Exception {
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubCategoryMockMvc.perform(get("/api/sub-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        restSubCategoryMockMvc.perform(get("/api/sub-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubCategory() throws Exception {
        // Initialize the database
        subCategoryService.save(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Update the subCategory
        SubCategory updatedSubCategory = subCategoryRepository.findById(subCategory.getId()).get();
        // Disconnect from session so that the updates on updatedSubCategory are not directly saved in db
        em.detach(updatedSubCategory);
        updatedSubCategory
            .label(UPDATED_LABEL);

        restSubCategoryMockMvc.perform(put("/api/sub-categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubCategory)))
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc.perform(put("/api/sub-categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategory)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubCategory() throws Exception {
        // Initialize the database
        subCategoryService.save(subCategory);

        int databaseSizeBeforeDelete = subCategoryRepository.findAll().size();

        // Delete the subCategory
        restSubCategoryMockMvc.perform(delete("/api/sub-categories/{id}", subCategory.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
