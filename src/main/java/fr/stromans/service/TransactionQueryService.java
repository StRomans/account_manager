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

import fr.stromans.domain.Transaction;
import fr.stromans.domain.*; // for static metamodels
import fr.stromans.repository.TransactionRepository;
import fr.stromans.service.dto.TransactionCriteria;

/**
 * Service for executing complex queries for {@link Transaction} entities in the database.
 * The main input is a {@link TransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Transaction} or a {@link Page} of {@link Transaction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionQueryService extends QueryService<Transaction> {

    private final Logger log = LoggerFactory.getLogger(TransactionQueryService.class);

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public TransactionQueryService(TransactionRepository transactionRepository,UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Transaction> findByCriteria(TransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findByCriteria(TransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.count(specification);
    }

    protected Specification<Transaction> createSpecification(List<TransactionCriteria> criterias) {
        Specification<Transaction> specification = this.createSpecification(new TransactionCriteria());

        for(TransactionCriteria criteria : criterias){
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Transaction_.date));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transaction_.amount));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Transaction_.label));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentifier(), Transaction_.identifier));
            }
            if (criteria.getBankAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBankAccountId(),
                    root -> root.join(Transaction_.bankAccount, JoinType.LEFT).get(BankAccount_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Transaction_.subCategory, JoinType.LEFT).get(SubCategory_.category).get(Category_.id)));
            }
            if (criteria.getSubCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSubCategoryId(),
                    root -> root.join(Transaction_.subCategory, JoinType.LEFT).get(SubCategory_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Transaction_.bankAccount, JoinType.LEFT).get(BankAccount_.owner).get(User_.id)));
            }
        }

        return specification;
    }

    /**
     * Function to convert {@link TransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transaction> createSpecification(TransactionCriteria criteria) {
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userService.findCurrentUser().isPresent()) {
            User loggedUser = userService.findCurrentUser().get();
            LongFilter ownerIdFilter = new LongFilter();
            ownerIdFilter.setEquals(loggedUser.getId());
            criteria.setOwnerId(ownerIdFilter);
        }

        Specification<Transaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Transaction_.date));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transaction_.amount));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Transaction_.label));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentifier(), Transaction_.identifier));
            }
            if (criteria.getBankAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBankAccountId(),
                    root -> root.join(Transaction_.bankAccount, JoinType.LEFT).get(BankAccount_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Transaction_.subCategory, JoinType.LEFT).get(SubCategory_.category).get(Category_.id)));
            }
            if (criteria.getSubCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSubCategoryId(),
                    root -> root.join(Transaction_.subCategory, JoinType.LEFT).get(SubCategory_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Transaction_.bankAccount, JoinType.LEFT).get(BankAccount_.owner).get(User_.id)));
            }
        }
        return specification;
    }
}
