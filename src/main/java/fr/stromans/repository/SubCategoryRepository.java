package fr.stromans.repository;

import fr.stromans.domain.SubCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SubCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>, JpaSpecificationExecutor<SubCategory> {
}
