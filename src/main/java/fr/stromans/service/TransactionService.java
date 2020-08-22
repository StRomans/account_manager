package fr.stromans.service;

import fr.stromans.domain.Transaction;
import fr.stromans.repository.TransactionRepository;
import fr.stromans.service.file.loader.IFileLoader;
import fr.stromans.service.file.loader.OfcFileLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        return transactionRepository.save(transaction);
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

    public List<Transaction> processFile(List<String> lines, String filename){
        IFileLoader fileLoader = null;
        if(FilenameUtils.getExtension(filename).equalsIgnoreCase("OFC")){
            fileLoader = new OfcFileLoader(lines);
        }
        else if (FilenameUtils.getExtension(filename).equalsIgnoreCase("OFX")){
            fileLoader = new OfcFileLoader(lines);
        }

        List<Transaction> transactionsToCreate = fileLoader.parse();
        List<Transaction> savedTransactions = new LinkedList<>();
        for (Transaction transaction : transactionsToCreate){
            savedTransactions.add(this.save(transaction));
        }

        return savedTransactions;
    }
}
