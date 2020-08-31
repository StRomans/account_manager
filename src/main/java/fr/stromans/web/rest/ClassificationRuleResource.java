package fr.stromans.web.rest;

import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.FilterRule;
import fr.stromans.domain.Transaction;
import fr.stromans.service.ClassificationRuleService;
import fr.stromans.service.TransactionService;
import fr.stromans.web.rest.errors.BadRequestAlertException;
import fr.stromans.service.dto.ClassificationRuleCriteria;
import fr.stromans.service.ClassificationRuleQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
 * REST controller for managing {@link fr.stromans.domain.ClassificationRule}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationRuleResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationRuleResource.class);

    private static final String ENTITY_NAME = "classificationRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationRuleService classificationRuleService;

    private final ClassificationRuleQueryService classificationRuleQueryService;

    private final TransactionService transactionService;

    public ClassificationRuleResource(ClassificationRuleService classificationRuleService,
                                      ClassificationRuleQueryService classificationRuleQueryService,
                                      TransactionService transactionService) {
        this.classificationRuleService = classificationRuleService;
        this.classificationRuleQueryService = classificationRuleQueryService;
        this.transactionService = transactionService;
    }

    /**
     * {@code POST  /classification-rules} : Create a new classificationRule.
     *
     * @param classificationRule the classificationRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classificationRule, or with status {@code 400 (Bad Request)} if the classificationRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classification-rules")
    public ResponseEntity<ClassificationRule> createClassificationRule(@Valid @RequestBody ClassificationRule classificationRule) throws URISyntaxException {
        log.debug("REST request to save ClassificationRule : {}", classificationRule);
        if (classificationRule.getId() != null) {
            throw new BadRequestAlertException("A new classificationRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassificationRule result = classificationRuleService.save(classificationRule);
        return ResponseEntity.created(new URI("/api/classification-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classification-rules} : Updates an existing classificationRule.
     *
     * @param classificationRule the classificationRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationRule,
     * or with status {@code 400 (Bad Request)} if the classificationRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classificationRule couldn't be updated.
     */
    @PutMapping("/classification-rules")
    public ResponseEntity<ClassificationRule> updateClassificationRule(@Valid @RequestBody ClassificationRule classificationRule) {
        log.debug("REST request to update ClassificationRule : {}", classificationRule);
        if (classificationRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassificationRule result = classificationRuleService.save(classificationRule);

        List<Transaction> transactionsToClassify = transactionService.findAllToClassify(result);
        for(Transaction transaction : transactionsToClassify){
            transaction.setSubCategory(result.getSubCategory());
            transactionService.save(transaction);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classificationRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classification-rules} : get all the classificationRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classificationRules in body.
     */
    @GetMapping("/classification-rules")
    public ResponseEntity<List<ClassificationRule>> getAllClassificationRules(ClassificationRuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassificationRules by criteria: {}", criteria);
        Page<ClassificationRule> page = classificationRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classification-rules/count} : count all the classificationRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/classification-rules/count")
    public ResponseEntity<Long> countClassificationRules(ClassificationRuleCriteria criteria) {
        log.debug("REST request to count ClassificationRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(classificationRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classification-rules/:id} : get the "id" classificationRule.
     *
     * @param id the id of the classificationRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classificationRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classification-rules/{id}")
    public ResponseEntity<ClassificationRule> getClassificationRule(@PathVariable Long id) {
        log.debug("REST request to get ClassificationRule : {}", id);
        Optional<ClassificationRule> classificationRule = classificationRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classificationRule);
    }

    /**
     * {@code DELETE  /classification-rules/:id} : delete the "id" classificationRule.
     *
     * @param id the id of the classificationRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classification-rules/{id}")
    public ResponseEntity<Void> deleteClassificationRule(@PathVariable Long id) {
        log.debug("REST request to delete ClassificationRule : {}", id);
        classificationRuleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
