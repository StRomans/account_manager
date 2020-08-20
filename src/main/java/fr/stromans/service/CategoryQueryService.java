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

import fr.stromans.domain.Category;
import fr.stromans.domain.*; // for static metamodels
import fr.stromans.repository.CategoryRepository;
import fr.stromans.service.dto.CategoryCriteria;

/**
 * Service for executing complex queries for {@link Category} entities in the database.
 * The main input is a {@link CategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Category} or a {@link Page} of {@link Category} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryQueryService extends QueryService<Category> {

    private final Logger log = LoggerFactory.getLogger(CategoryQueryService.class);

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    public CategoryQueryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link Category} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findByCriteria(CategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Category} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Category> findByCriteria(CategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Category> createSpecification(CategoryCriteria criteria) {
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userService.findCurrentUser().isPresent()) {
            User loggedUser = userService.findCurrentUser().get();
            LongFilter ownerIdFilter = new LongFilter();
            ownerIdFilter.setEquals(loggedUser.getId());
            criteria.setOwnerId(ownerIdFilter);
        }

        Specification<Category> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Category_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Category_.label));
            }
            if (criteria.getPrimaryColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimaryColor(), Category_.primaryColor));
            }
            if (criteria.getSecondaryColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSecondaryColor(), Category_.secondaryColor));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Category_.owner, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
