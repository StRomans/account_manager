package fr.stromans.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import fr.stromans.security.AuthoritiesConstants;
import fr.stromans.security.SecurityUtils;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.*; // for static metamodels
import fr.stromans.repository.ClassificationRuleRepository;
import fr.stromans.service.dto.ClassificationRuleCriteria;

/**
 * Service for executing complex queries for {@link ClassificationRule} entities in the database.
 * The main input is a {@link ClassificationRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassificationRule} or a {@link Page} of {@link ClassificationRule} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationRuleQueryService extends QueryService<ClassificationRule> {

    private final Logger log = LoggerFactory.getLogger(ClassificationRuleQueryService.class);

    private final ClassificationRuleRepository classificationRuleRepository;

    private final UserService userService;

    public ClassificationRuleQueryService(ClassificationRuleRepository classificationRuleRepository, UserService userService) {
        this.classificationRuleRepository = classificationRuleRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link ClassificationRule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassificationRule> findByCriteria(ClassificationRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassificationRule> specification = createSpecification(criteria);
        return classificationRuleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassificationRule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationRule> findByCriteria(ClassificationRuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassificationRule> specification = createSpecification(criteria);
        return classificationRuleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassificationRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassificationRule> specification = createSpecification(criteria);
        return classificationRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassificationRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassificationRule> createSpecification(ClassificationRuleCriteria criteria) {
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userService.findCurrentUser().isPresent()) {
            User loggedUser = userService.findCurrentUser().get();
            LongFilter ownerIdFilter = new LongFilter();
            ownerIdFilter.setEquals(loggedUser.getId());
            criteria.setOwnerId(ownerIdFilter);
        }

        Specification<ClassificationRule> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassificationRule_.id));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(ClassificationRule_.owner, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBankAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBankAccountId(),
                    root -> root.join(ClassificationRule_.bankAccount, JoinType.LEFT).get(BankAccount_.id)));
            }
            if (criteria.getTransactionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionsId(),
                    root -> root.join(ClassificationRule_.transactions, JoinType.LEFT).get(Transaction_.id)));
            }
            if (criteria.getFilterRulesId() != null) {
                specification = specification.and(buildSpecification(criteria.getFilterRulesId(),
                    root -> root.join(ClassificationRule_.filterRules, JoinType.LEFT).get(FilterRule_.id)));
            }
        }
        return specification;
    }
}
