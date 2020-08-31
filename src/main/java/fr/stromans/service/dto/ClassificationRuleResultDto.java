package fr.stromans.service.dto;

import fr.stromans.domain.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ClassificationRuleResultDto {

    private List<Transaction> transactionsToClassify = new ArrayList<>();

    public ClassificationRuleResultDto() {
        // Empty constructor needed for Jackson.
    }

    public List<Transaction> getTransactionsToClassify() {
        return transactionsToClassify;
    }

    public void setTransactionsToClassify(List<Transaction> transactionsToClassify) {
        this.transactionsToClassify = transactionsToClassify;
    }

    public void appendIgnoredTransaction(Transaction transaction){
        this.transactionsToClassify.add(transaction);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationRuleResultDto{" +
            ", transactionsToClassify=" + transactionsToClassify +
            "}";
    }
}
