package fr.stromans.repository;

import fr.stromans.domain.BankAccount;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {

    @Query("select bankAccount from BankAccount bankAccount where bankAccount.owner.login = ?#{principal.username}")
    List<BankAccount> findByOwnerIsCurrentUser();
}
