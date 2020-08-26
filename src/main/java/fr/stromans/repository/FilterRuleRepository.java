package fr.stromans.repository;

import fr.stromans.domain.FilterRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FilterRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilterRuleRepository extends JpaRepository<FilterRule, Long> {
}
