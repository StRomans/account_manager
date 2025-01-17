package fr.stromans.web.rest;

import fr.stromans.domain.BankAccount;
import fr.stromans.domain.ClassificationRule;
import fr.stromans.domain.Transaction;
import fr.stromans.service.BankAccountService;
import fr.stromans.service.TransactionService;
import fr.stromans.service.dto.ClassificationRuleResultDto;
import fr.stromans.service.dto.UploadTransactionResultDTO;
import fr.stromans.web.rest.errors.BadRequestAlertException;
import fr.stromans.service.dto.TransactionCriteria;
import fr.stromans.service.TransactionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link fr.stromans.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionQueryService transactionQueryService;

    private final BankAccountService bankAccountService;

    public TransactionResource(TransactionService transactionService, TransactionQueryService transactionQueryService, BankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.transactionQueryService = transactionQueryService;
        this.bankAccountService = bankAccountService;
    }

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transaction the transaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        if (transaction.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transaction result = transactionService.save(transaction);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions} : Updates an existing transaction.
     *
     * @param transaction the transaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaction,
     * or with status {@code 400 (Bad Request)} if the transaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Transaction result = transactionService.save(transaction);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transactions/upload} : upload transactions file.
     *
     * @param file the File to process.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @PostMapping(value = "/transactions/upload")
    public ResponseEntity<UploadTransactionResultDTO> upload(@RequestParam(value = "file") MultipartFile file,
                                                             @RequestParam(value = "bankAccountId") String bankAccountId) {
        log.debug("REST request to create transactions batch from file : {}", file.getOriginalFilename());

        BankAccount bankAccount = bankAccountService.findOne(Long.parseLong(bankAccountId)).get();

        File tempFile = null;
        List<String> lineList = null;
        try {
            tempFile = File.createTempFile("upload_transaction_", null);
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            lineList = FileUtils.readLines(tempFile, Charset.defaultCharset());
        } catch (IOException e) {
            log.error("Unable to read file {}", file.getOriginalFilename());
        }
        finally {
            if (null != tempFile) tempFile.delete();
        }

        return ResponseEntity.ok().body(transactionService.processFile(bankAccount, lineList, file.getOriginalFilename()));
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(TransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Transactions by criteria: {}", criteria);
        Page<Transaction> page = transactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/transactions/classification/estimate")
    public ResponseEntity<ClassificationRuleResultDto> findAllToClassify(@Valid @RequestBody ClassificationRule classificationRule){
        log.debug("REST request to get all Transactions to classify by classificationRule: {}", classificationRule);
        ClassificationRuleResultDto resultDto = new ClassificationRuleResultDto();
        resultDto.setTransactionsToClassify(transactionService.findAllToClassify(classificationRule));
        return ResponseEntity.ok().body(resultDto);
    }

    /**
     * {@code GET  /transactions/count} : count all the transactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transactions/count")
    public ResponseEntity<Long> countTransactions(TransactionCriteria criteria) {
        log.debug("REST request to count Transactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
