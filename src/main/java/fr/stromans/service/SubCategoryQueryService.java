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

import fr.stromans.domain.SubCategory;
import fr.stromans.domain.*; // for static metamodels
import fr.stromans.repository.SubCategoryRepository;
import fr.stromans.service.dto.SubCategoryCriteria;

/**
 * Service for executing complex queries for {@link SubCategory} entities in the database.
 * The main input is a {@link SubCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubCategory} or a {@link Page} of {@link SubCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubCategoryQueryService extends QueryService<SubCategory> {

    private final Logger log = LoggerFactory.getLogger(SubCategoryQueryService.class);

    private final SubCategoryRepository subCategoryRepository;

    private final UserService userService;

    public SubCategoryQueryService(SubCategoryRepository subCategoryRepository, UserService userService) {
        this.subCategoryRepository = subCategoryRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link SubCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubCategory> findByCriteria(SubCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SubCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubCategory> findByCriteria(SubCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link SubCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubCategory> createSpecification(SubCategoryCriteria criteria) {
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userService.findCurrentUser().isPresent()) {
            User loggedUser = userService.findCurrentUser().get();
            LongFilter ownerIdFilter = new LongFilter();
            ownerIdFilter.setEquals(loggedUser.getId());
            criteria.setOwnerId(ownerIdFilter);
        }

        Specification<SubCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubCategory_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), SubCategory_.label));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(SubCategory_.category, JoinType.INNER).get(Category_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(SubCategory_.category, JoinType.INNER).get(Category_.owner).get(User_.id)));
            }
        }
        return specification;
    }
}
