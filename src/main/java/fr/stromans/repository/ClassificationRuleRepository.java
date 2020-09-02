package fr.stromans.repository;

import fr.stromans.domain.BankAccount;
import fr.stromans.domain.ClassificationRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ClassificationRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRuleRepository extends JpaRepository<ClassificationRule, Long>, JpaSpecificationExecutor<ClassificationRule> {

    @Query("select classificationRule from ClassificationRule classificationRule where classificationRule.owner.login = ?#{principal.username}")
    List<ClassificationRule> findByOwnerIsCurrentUser();

    List<ClassificationRule> findAllByBankAccountOrderByPriorityDesc(BankAccount bankAccount);
}
