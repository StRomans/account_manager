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

import fr.stromans.domain.BankAccount;
import fr.stromans.domain.*; // for static metamodels
import fr.stromans.repository.BankAccountRepository;
import fr.stromans.service.dto.BankAccountCriteria;

/**
 * Service for executing complex queries for {@link BankAccount} entities in the database.
 * The main input is a {@link BankAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankAccount} or a {@link Page} of {@link BankAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankAccountQueryService extends QueryService<BankAccount> {

    private final Logger log = LoggerFactory.getLogger(BankAccountQueryService.class);

    private final BankAccountRepository bankAccountRepository;

    private final UserService userService;

    public BankAccountQueryService(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link BankAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankAccount> findByCriteria(BankAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BankAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankAccount> findByCriteria(BankAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link BankAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankAccount> createSpecification(BankAccountCriteria criteria) {
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userService.findCurrentUser().isPresent()) {
            User loggedUser = userService.findCurrentUser().get();
            LongFilter ownerIdFilter = new LongFilter();
            ownerIdFilter.setEquals(loggedUser.getId());
            criteria.setOwnerId(ownerIdFilter);
        }

        Specification<BankAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BankAccount_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), BankAccount_.label));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrencyId(),
                    root -> root.join(BankAccount_.currency, JoinType.LEFT).get(Currency_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(BankAccount_.owner, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), BankAccount_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), BankAccount_.lastModifiedBy));
            }
        }
        return specification;
    }
}
