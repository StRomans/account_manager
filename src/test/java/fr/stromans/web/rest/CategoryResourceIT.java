package fr.stromans.web.rest;

import fr.stromans.AccountManagerApp;
import fr.stromans.domain.Category;
import fr.stromans.domain.User;
import fr.stromans.repository.CategoryRepository;
import fr.stromans.service.CategoryService;
import fr.stromans.service.dto.CategoryCriteria;
import fr.stromans.service.CategoryQueryService;

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
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@SpringBootTest(classes = AccountManagerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CategoryResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_COLOR = "BBBBBBBBBB";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .label(DEFAULT_LABEL)
            .primaryColor(DEFAULT_PRIMARY_COLOR)
            .secondaryColor(DEFAULT_SECONDARY_COLOR);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        category.setOwner(user);
        return category;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .label(UPDATED_LABEL)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        category.setOwner(user);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        restCategoryMockMvc.perform(post("/api/categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCategory.getPrimaryColor()).isEqualTo(DEFAULT_PRIMARY_COLOR);
        assertThat(testCategory.getSecondaryColor()).isEqualTo(DEFAULT_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void createCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category with an existing ID
        category.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc.perform(post("/api/categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setLabel(null);

        // Create the Category, which fails.


        restCategoryMockMvc.perform(post("/api/categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)));
    }
    
    @Test
    @Transactional
    public void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.primaryColor").value(DEFAULT_PRIMARY_COLOR))
            .andExpect(jsonPath("$.secondaryColor").value(DEFAULT_SECONDARY_COLOR));
    }


    @Test
    @Transactional
    public void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        Long id = category.getId();

        defaultCategoryShouldBeFound("id.equals=" + id);
        defaultCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCategoriesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label equals to DEFAULT_LABEL
        defaultCategoryShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the categoryList where label equals to UPDATED_LABEL
        defaultCategoryShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label not equals to DEFAULT_LABEL
        defaultCategoryShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the categoryList where label not equals to UPDATED_LABEL
        defaultCategoryShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultCategoryShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the categoryList where label equals to UPDATED_LABEL
        defaultCategoryShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label is not null
        defaultCategoryShouldBeFound("label.specified=true");

        // Get all the categoryList where label is null
        defaultCategoryShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByLabelContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label contains DEFAULT_LABEL
        defaultCategoryShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the categoryList where label contains UPDATED_LABEL
        defaultCategoryShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where label does not contain DEFAULT_LABEL
        defaultCategoryShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the categoryList where label does not contain UPDATED_LABEL
        defaultCategoryShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor equals to DEFAULT_PRIMARY_COLOR
        defaultCategoryShouldBeFound("primaryColor.equals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the categoryList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultCategoryShouldNotBeFound("primaryColor.equals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor not equals to DEFAULT_PRIMARY_COLOR
        defaultCategoryShouldNotBeFound("primaryColor.notEquals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the categoryList where primaryColor not equals to UPDATED_PRIMARY_COLOR
        defaultCategoryShouldBeFound("primaryColor.notEquals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor in DEFAULT_PRIMARY_COLOR or UPDATED_PRIMARY_COLOR
        defaultCategoryShouldBeFound("primaryColor.in=" + DEFAULT_PRIMARY_COLOR + "," + UPDATED_PRIMARY_COLOR);

        // Get all the categoryList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultCategoryShouldNotBeFound("primaryColor.in=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor is not null
        defaultCategoryShouldBeFound("primaryColor.specified=true");

        // Get all the categoryList where primaryColor is null
        defaultCategoryShouldNotBeFound("primaryColor.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor contains DEFAULT_PRIMARY_COLOR
        defaultCategoryShouldBeFound("primaryColor.contains=" + DEFAULT_PRIMARY_COLOR);

        // Get all the categoryList where primaryColor contains UPDATED_PRIMARY_COLOR
        defaultCategoryShouldNotBeFound("primaryColor.contains=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesByPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where primaryColor does not contain DEFAULT_PRIMARY_COLOR
        defaultCategoryShouldNotBeFound("primaryColor.doesNotContain=" + DEFAULT_PRIMARY_COLOR);

        // Get all the categoryList where primaryColor does not contain UPDATED_PRIMARY_COLOR
        defaultCategoryShouldBeFound("primaryColor.doesNotContain=" + UPDATED_PRIMARY_COLOR);
    }


    @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor equals to DEFAULT_SECONDARY_COLOR
        defaultCategoryShouldBeFound("secondaryColor.equals=" + DEFAULT_SECONDARY_COLOR);

        // Get all the categoryList where secondaryColor equals to UPDATED_SECONDARY_COLOR
        defaultCategoryShouldNotBeFound("secondaryColor.equals=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor not equals to DEFAULT_SECONDARY_COLOR
        defaultCategoryShouldNotBeFound("secondaryColor.notEquals=" + DEFAULT_SECONDARY_COLOR);

        // Get all the categoryList where secondaryColor not equals to UPDATED_SECONDARY_COLOR
        defaultCategoryShouldBeFound("secondaryColor.notEquals=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor in DEFAULT_SECONDARY_COLOR or UPDATED_SECONDARY_COLOR
        defaultCategoryShouldBeFound("secondaryColor.in=" + DEFAULT_SECONDARY_COLOR + "," + UPDATED_SECONDARY_COLOR);

        // Get all the categoryList where secondaryColor equals to UPDATED_SECONDARY_COLOR
        defaultCategoryShouldNotBeFound("secondaryColor.in=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor is not null
        defaultCategoryShouldBeFound("secondaryColor.specified=true");

        // Get all the categoryList where secondaryColor is null
        defaultCategoryShouldNotBeFound("secondaryColor.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor contains DEFAULT_SECONDARY_COLOR
        defaultCategoryShouldBeFound("secondaryColor.contains=" + DEFAULT_SECONDARY_COLOR);

        // Get all the categoryList where secondaryColor contains UPDATED_SECONDARY_COLOR
        defaultCategoryShouldNotBeFound("secondaryColor.contains=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllCategoriesBySecondaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where secondaryColor does not contain DEFAULT_SECONDARY_COLOR
        defaultCategoryShouldNotBeFound("secondaryColor.doesNotContain=" + DEFAULT_SECONDARY_COLOR);

        // Get all the categoryList where secondaryColor does not contain UPDATED_SECONDARY_COLOR
        defaultCategoryShouldBeFound("secondaryColor.doesNotContain=" + UPDATED_SECONDARY_COLOR);
    }


    @Test
    @Transactional
    public void getAllCategoriesByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = category.getOwner();
        categoryRepository.saveAndFlush(category);
        Long ownerId = owner.getId();

        // Get all the categoryList where owner equals to ownerId
        defaultCategoryShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the categoryList where owner equals to ownerId + 1
        defaultCategoryShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)));

        // Check, that the count call also returns 1
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategory() throws Exception {
        // Initialize the database
        categoryService.save(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .label(UPDATED_LABEL)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR);

        restCategoryMockMvc.perform(put("/api/categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategory)))
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCategory.getPrimaryColor()).isEqualTo(UPDATED_PRIMARY_COLOR);
        assertThat(testCategory.getSecondaryColor()).isEqualTo(UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc.perform(put("/api/categories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategory() throws Exception {
        // Initialize the database
        categoryService.save(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc.perform(delete("/api/categories/{id}", category.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
