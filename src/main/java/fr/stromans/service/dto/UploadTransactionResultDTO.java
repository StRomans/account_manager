package fr.stromans.service.dto;

import fr.stromans.domain.Transaction;

import java.util.HashSet;
import java.util.Set;

public class UploadTransactionResultDTO {

    private Set<Transaction> ignoredTransactions = new HashSet<>();
    private Set<Transaction> savedTransactions = new HashSet<>();

    public UploadTransactionResultDTO() {
        // Empty constructor needed for Jackson.
    }

    public Set<Transaction> getIgnoredTransactions() {
        return ignoredTransactions;
    }

    public void setIgnoredTransactions(Set<Transaction> ignoredTransactions) {
        this.ignoredTransactions = ignoredTransactions;
    }

    public Set<Transaction> getSavedTransactions() {
        return savedTransactions;
    }

    public void setSavedTransactions(Set<Transaction> savedTransactions) {
        this.savedTransactions = savedTransactions;
    }

    public void appendSavedTransaction(Transaction transaction){
        this.savedTransactions.add(transaction);
    }

    public void appendIgnoredTransaction(Transaction transaction){
        this.ignoredTransactions.add(transaction);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadTransactionResultDTO{" +
            "savedTransactions=" + savedTransactions +
            ", ignoredTransactions=" + ignoredTransactions +
            "}";
    }
}
