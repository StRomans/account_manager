package fr.stromans.service;

import fr.stromans.domain.BankAccount;
import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.Transaction;
import fr.stromans.repository.ClassificationRuleRepository;
import fr.stromans.repository.TransactionRepository;
import fr.stromans.service.dto.UploadTransactionResultDTO;
import fr.stromans.service.file.loader.IFileLoader;
import fr.stromans.service.file.loader.OfcFileLoader;
import fr.stromans.service.file.loader.OfxFileLoader;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final ClassificationRuleRepository classificationRuleRepository;

    public TransactionService(TransactionRepository transactionRepository, ClassificationRuleRepository classificationRuleRepository) {
        this.transactionRepository = transactionRepository;
        this.classificationRuleRepository = classificationRuleRepository;
    }

    public List<Transaction> findAllToClassify(ClassificationRule classificationRule) {

        List<Transaction> unclassifiedTransactionList = transactionRepository.findAllByBankAccountAndSubCategoryIsNull(classificationRule.getBankAccount());

        return unclassifiedTransactionList.stream().filter(transaction -> classificationRule.isMatching(transaction)).collect(Collectors.toList());
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        Transaction savedTransation = transactionRepository.save(transaction);

        if(null == savedTransation.getSubCategory()){
            List<ClassificationRule> rules = classificationRuleRepository.findAllByBankAccountOrderByPriorityDesc(transaction.getBankAccount());
            for(ClassificationRule rule : rules){
                if(rule.isMatching(savedTransation)){
                    savedTransation.setSubCategory(rule.getSubCategory());
                    return transactionRepository.save(savedTransation);
                }
            }
        }

        return savedTransation;
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
    }


    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    /**
     * Parse provided lines (coming from a file) to add Transactions on a provided Bank Account
     * @param bankAccount the {@link BankAccount} on which the {@link Transaction} should be created
     * @param lines of the uploaded {@link java.io.File}. Stands for a bunch of {@link Transaction}
     * @param filename of the uploaded {@link java.io.File}
     * @return created Transactions
     */
    public UploadTransactionResultDTO processFile(BankAccount bankAccount, List<String> lines, String filename){
        IFileLoader fileLoader = null;
        UploadTransactionResultDTO resultDTO = new UploadTransactionResultDTO();

        if(FilenameUtils.getExtension(filename).equalsIgnoreCase("OFC")){
            fileLoader = new OfcFileLoader(lines);
        }
        else if (FilenameUtils.getExtension(filename).equalsIgnoreCase("OFX")){
            fileLoader = new OfxFileLoader(lines);
        }

        assert fileLoader != null;
        List<Transaction> transactionsToCreate = fileLoader.parse();
        for (Transaction transaction : transactionsToCreate){
            try {
                transaction.setBankAccount(bankAccount);
                boolean canBeSaved = null == transaction.getIdentifier() || null == transactionRepository.findOneByBankAccountAndIdentifier(bankAccount, transaction.getIdentifier());
                if(canBeSaved) {
                    Transaction savedTransaction = this.save(transaction);
                    resultDTO.appendSavedTransaction(savedTransaction);
                } else {
                    resultDTO.appendIgnoredTransaction(transaction);
                }
            } catch (Exception e){
                resultDTO.appendIgnoredTransaction(transaction);
            }
        }

        return resultDTO;
    }
}
