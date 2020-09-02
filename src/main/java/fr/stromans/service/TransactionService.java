package fr.stromans.service;

import fr.stromans.domain.*;
import fr.stromans.domain.enumeration.RuleField;
import fr.stromans.domain.enumeration.RuleOperator;
import fr.stromans.repository.ClassificationRuleRepository;
import fr.stromans.repository.TransactionRepository;
import fr.stromans.service.dto.TransactionCriteria;
import fr.stromans.service.dto.UploadTransactionResultDTO;
import fr.stromans.service.file.loader.IFileLoader;
import fr.stromans.service.file.loader.OfcFileLoader;
import fr.stromans.service.file.loader.OfxFileLoader;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final TransactionQueryService transactionQueryService;

    private final ClassificationRuleRepository classificationRuleRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionQueryService transactionQueryService, ClassificationRuleRepository classificationRuleRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionQueryService = transactionQueryService;
        this.classificationRuleRepository = classificationRuleRepository;
    }

    public List<Transaction> findAllToClassify(ClassificationRule classificationRule) {

        List<TransactionCriteria> criterias = new ArrayList<>();
        // Filter on unclassified transactions
        TransactionCriteria unclassifiedCriteria = new TransactionCriteria();
        LongFilter subCategoryFilter = new LongFilter();
        subCategoryFilter.setSpecified(false);
        unclassifiedCriteria.setSubCategoryId(subCategoryFilter);
        criterias.add(unclassifiedCriteria);

        // Filter on Bank Account
        TransactionCriteria bankCriteria = new TransactionCriteria();
        LongFilter bankFilter = new LongFilter();
        bankFilter.setEquals(classificationRule.getBankAccount().getId());
        bankCriteria.setBankAccountId(bankFilter);
        criterias.add(bankCriteria);

        // Apply any other condition (at least one)
        for(FilterRule filterRule : classificationRule.getFilterRules()){

            if(RuleField.AMOUNT.equals((filterRule.getField()))){
                TransactionCriteria amountCriteria = new TransactionCriteria();
                BigDecimal value = filterRule.getNumberValue();
                BigDecimalFilter amountFilter = new BigDecimalFilter();
                if(RuleOperator.EQUALS.equals(filterRule.getOperator())) amountFilter.setEquals(value);
                if(RuleOperator.LESSOREQUALTHAN.equals(filterRule.getOperator())) amountFilter.setLessThanOrEqual(value);
                if(RuleOperator.GREATEROREQUALTHAN.equals(filterRule.getOperator())) amountFilter.setGreaterThanOrEqual(value);
                amountCriteria.setAmount(amountFilter);
                criterias.add(amountCriteria);
            }
            else if (RuleField.DATE.equals((filterRule.getField()))){
                TransactionCriteria dateCriteria = new TransactionCriteria();
                LocalDate value = filterRule.getDateValue();
                LocalDateFilter dateFilter = new LocalDateFilter();
                if(RuleOperator.EQUALS.equals(filterRule.getOperator())) dateFilter.setEquals(value);
                if(RuleOperator.LESSOREQUALTHAN.equals(filterRule.getOperator())) dateFilter.setLessThanOrEqual(value);
                if(RuleOperator.GREATEROREQUALTHAN.equals(filterRule.getOperator())) dateFilter.setGreaterThanOrEqual(value);
                dateCriteria.setDate(dateFilter);
                criterias.add(dateCriteria);
            }
            else if (RuleField.LABEL.equals((filterRule.getField()))){
                TransactionCriteria labelCriteria = new TransactionCriteria();
                StringFilter labelFilter = new StringFilter();
                if(RuleOperator.EQUALS.equals(filterRule.getOperator())) labelFilter.setEquals(filterRule.getStringValue().toUpperCase());
                if(RuleOperator.CONTAINS.equals(filterRule.getOperator())) labelFilter.setContains(filterRule.getStringValue().toUpperCase());
                labelCriteria.setLabel(labelFilter);
                criterias.add(labelCriteria);
            }
        }

        final Specification<Transaction> specification = transactionQueryService.createSpecification(criterias);
        List<Transaction> matchingTransactions = transactionRepository.findAll(specification);

        return matchingTransactions;
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
                if(this.findAllToClassify(rule).contains(savedTransation)){
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

        List<Transaction> transactionsToCreate = fileLoader.parse();
        for (Transaction transaction : transactionsToCreate){
            try {
                transaction.setBankAccount(bankAccount);
                boolean canBeSaved = (null != transaction.getIdentifier() && null == transactionRepository.findOneByBankAccountAndIdentifier(bankAccount, transaction.getIdentifier())) || null == transaction.getIdentifier();
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
