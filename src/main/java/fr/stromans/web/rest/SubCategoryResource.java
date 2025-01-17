package fr.stromans.web.rest;

import fr.stromans.domain.Category;
import fr.stromans.domain.SubCategory;
import fr.stromans.service.SubCategoryService;
import fr.stromans.web.rest.errors.BadRequestAlertException;
import fr.stromans.service.dto.SubCategoryCriteria;
import fr.stromans.service.SubCategoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.stromans.domain.SubCategory}.
 */
@RestController
@RequestMapping("/api")
public class SubCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCategoryService subCategoryService;

    private final SubCategoryQueryService subCategoryQueryService;

    public SubCategoryResource(SubCategoryService subCategoryService, SubCategoryQueryService subCategoryQueryService) {
        this.subCategoryService = subCategoryService;
        this.subCategoryQueryService = subCategoryQueryService;
    }

    /**
     * {@code POST  /sub-categories} : Create a new subCategory.
     *
     * @param subCategory the subCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategory, or with status {@code 400 (Bad Request)} if the subCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-categories")
    public ResponseEntity<SubCategory> createSubCategory(@Valid @RequestBody SubCategory subCategory) throws URISyntaxException {
        log.debug("REST request to save SubCategory : {}", subCategory);
        if (subCategory.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCategory result = subCategoryService.save(subCategory);
        return ResponseEntity.created(new URI("/api/sub-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-categories} : Updates an existing subCategory.
     *
     * @param subCategory the subCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategory,
     * or with status {@code 400 (Bad Request)} if the subCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-categories")
    public ResponseEntity<SubCategory> updateSubCategory(@Valid @RequestBody SubCategory subCategory) throws URISyntaxException {
        log.debug("REST request to update SubCategory : {}", subCategory);
        if (subCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubCategory result = subCategoryService.save(subCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("/sub-categories")
    public ResponseEntity<List<SubCategory>> getAllSubCategories(SubCategoryCriteria criteria) {
        log.debug("REST request to get SubCategories by criteria: {}", criteria);
        List<SubCategory> entityList = subCategoryQueryService.findByCriteria(criteria);

        Collections.sort(entityList, Comparator.comparing(SubCategory::getLabel));

        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sub-categories/count} : count all the subCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sub-categories/count")
    public ResponseEntity<Long> countSubCategories(SubCategoryCriteria criteria) {
        log.debug("REST request to count SubCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(subCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable Long id) {
        log.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategory> subCategory = subCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategory);
    }

    /**
     * {@code DELETE  /sub-categories/:id} : delete the "id" subCategory.
     *
     * @param id the id of the subCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-categories/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        log.debug("REST request to delete SubCategory : {}", id);
        subCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
