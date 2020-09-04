package fr.stromans.repository;

import fr.stromans.domain.BankAccount;
import fr.stromans.domain.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("select transaction from Transaction transaction where transaction.bankAccount.owner.login = ?#{principal.username}")
    Page<Transaction> findByOwnedBankAccounts(Pageable pageable);

    Transaction findOneByBankAccountAndIdentifier(BankAccount bankAccount, String identifier);

    List<Transaction> findAllByBankAccountAndSubCategoryIsNull(BankAccount bankAccount);
}
