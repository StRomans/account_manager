package fr.stromans.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassificationRule.
 */
@Entity
@Table(name = "classification_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassificationRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "apply_to_unclassified")
    private Boolean applyToUnclassified;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "classificationRules", allowSetters = true)
    private User owner;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @NotNull
    @JsonIgnoreProperties(value = "classificationRules", allowSetters = true)
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "classificationRule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "classificationRule", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<FilterRule> filterRules = new HashSet<>();

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @NotNull
    @JsonIgnoreProperties(value = "classificationRules", allowSetters = true)
    private SubCategory subCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public ClassificationRule priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean isApplyToUnclassified() {
        return applyToUnclassified;
    }

    public ClassificationRule applyToUnclassified(Boolean applyToUnclassified) {
        this.applyToUnclassified = applyToUnclassified;
        return this;
    }

    public void setApplyToUnclassified(Boolean applyToUnclassified) {
        this.applyToUnclassified = applyToUnclassified;
    }

    public User getOwner() {
        return owner;
    }

    public ClassificationRule owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public ClassificationRule bankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public ClassificationRule transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public ClassificationRule addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setClassificationRule(this);
        return this;
    }

    public ClassificationRule removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setClassificationRule(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<FilterRule> getFilterRules() {
        return filterRules;
    }

    public ClassificationRule filterRules(Set<FilterRule> filterRules) {
        this.filterRules = filterRules;
        return this;
    }

    public ClassificationRule addFilterRules(FilterRule filterRule) {
        this.filterRules.add(filterRule);
        filterRule.setClassificationRule(this);
        return this;
    }

    public ClassificationRule removeFilterRules(FilterRule filterRule) {
        this.filterRules.remove(filterRule);
        filterRule.setClassificationRule(null);
        return this;
    }

    public void setFilterRules(Set<FilterRule> filterRules) {
        for (FilterRule oldFilterRule : this.filterRules){
            this.removeFilterRules(oldFilterRule);
        }
        for (FilterRule newFilterRule : filterRules){
            this.addFilterRules(newFilterRule);
        }
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public ClassificationRule subCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
        return this;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationRule)) {
            return false;
        }
        return id != null && id.equals(((ClassificationRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationRule{" +
            "id=" + getId() +
            ", priority=" + getPriority() +
            ", applyToUnclassified='" + isApplyToUnclassified() + "'" +
            "}";
    }

    public boolean isMatching(Transaction transaction){
        return this.getFilterRules().stream().allMatch(filterRule -> filterRule.isMatching(transaction));
    }
}
