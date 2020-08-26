package fr.stromans.service;

import fr.stromans.domain.ClassificationRule;
import fr.stromans.repository.ClassificationRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassificationRule}.
 */
@Service
@Transactional
public class ClassificationRuleService {

    private final Logger log = LoggerFactory.getLogger(ClassificationRuleService.class);

    private final ClassificationRuleRepository classificationRuleRepository;

    public ClassificationRuleService(ClassificationRuleRepository classificationRuleRepository) {
        this.classificationRuleRepository = classificationRuleRepository;
    }

    /**
     * Save a classificationRule.
     *
     * @param classificationRule the entity to save.
     * @return the persisted entity.
     */
    public ClassificationRule save(ClassificationRule classificationRule) {
        log.debug("Request to save ClassificationRule : {}", classificationRule);
        return classificationRuleRepository.save(classificationRule);
    }

    /**
     * Get all the classificationRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationRule> findAll(Pageable pageable) {
        log.debug("Request to get all ClassificationRules");
        return classificationRuleRepository.findAll(pageable);
    }


    /**
     * Get one classificationRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassificationRule> findOne(Long id) {
        log.debug("Request to get ClassificationRule : {}", id);
        return classificationRuleRepository.findById(id);
    }

    /**
     * Delete the classificationRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassificationRule : {}", id);
        classificationRuleRepository.deleteById(id);
    }
}
