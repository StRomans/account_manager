package fr.stromans.web.rest;

import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.FilterRule;
import fr.stromans.domain.SubCategory;
import fr.stromans.repository.ClassificationRuleRepository;
import fr.stromans.repository.FilterRuleRepository;
import fr.stromans.web.rest.errors.BadRequestAlertException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.stromans.domain.FilterRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FilterRuleResource {

    private final Logger log = LoggerFactory.getLogger(FilterRuleResource.class);

    private static final String ENTITY_NAME = "filterRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilterRuleRepository filterRuleRepository;

    private final ClassificationRuleRepository classificationRuleRepository;

    public FilterRuleResource(FilterRuleRepository filterRuleRepository, ClassificationRuleRepository classificationRuleRepository) {
        this.filterRuleRepository = filterRuleRepository;
        this.classificationRuleRepository = classificationRuleRepository;
    }

    /**
     * {@code POST  /filter-rules} : Create a new filterRule.
     *
     * @param filterRule the filterRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filterRule, or with status {@code 400 (Bad Request)} if the filterRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filter-rules")
    public ResponseEntity<FilterRule> createFilterRule(@Valid @RequestBody FilterRule filterRule) throws URISyntaxException {
        log.debug("REST request to save FilterRule : {}", filterRule);
        if (filterRule.getId() != null) {
            throw new BadRequestAlertException("A new filterRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilterRule result = filterRuleRepository.save(filterRule);
        ClassificationRule classificationRule = classificationRuleRepository.findById(filterRule.getClassificationRule().getId()).get();
        classificationRule.addFilterRules(result);
        classificationRuleRepository.save(classificationRule);
        return ResponseEntity.created(new URI("/api/filter-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filter-rules} : Updates an existing filterRule.
     *
     * @param filterRule the filterRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filterRule,
     * or with status {@code 400 (Bad Request)} if the filterRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filterRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filter-rules")
    public ResponseEntity<FilterRule> updateFilterRule(@Valid @RequestBody FilterRule filterRule) throws URISyntaxException {
        log.debug("REST request to update FilterRule : {}", filterRule);
        if (filterRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FilterRule result = filterRuleRepository.save(filterRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filterRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /filter-rules} : get all the filterRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filterRules in body.
     */
    @GetMapping("/filter-rules")
    public ResponseEntity<List<FilterRule>> getAllFilterRules(Pageable pageable) {
        log.debug("REST request to get a page of FilterRules");
        Page<FilterRule> page = filterRuleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        Collections.sort(page.getContent(), Comparator.comparing(FilterRule::getField));

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filter-rules/:id} : get the "id" filterRule.
     *
     * @param id the id of the filterRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filterRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filter-rules/{id}")
    public ResponseEntity<FilterRule> getFilterRule(@PathVariable Long id) {
        log.debug("REST request to get FilterRule : {}", id);
        Optional<FilterRule> filterRule = filterRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filterRule);
    }

    /**
     * {@code DELETE  /filter-rules/:id} : delete the "id" filterRule.
     *
     * @param id the id of the filterRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filter-rules/{id}")
    public ResponseEntity<Void> deleteFilterRule(@PathVariable Long id) {
        log.debug("REST request to delete FilterRule : {}", id);
        FilterRule ruleToDelete = filterRuleRepository.findById(id).get();
        ClassificationRule classificationRule = classificationRuleRepository.findById(ruleToDelete.getClassificationRule().getId()).get();
        classificationRule.removeFilterRules(ruleToDelete);
        classificationRuleRepository.save(classificationRule);
        filterRuleRepository.delete(ruleToDelete);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
