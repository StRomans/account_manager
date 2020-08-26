package fr.stromans.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link fr.stromans.domain.Transaction} entity. This class is used
 * in {@link fr.stromans.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private BigDecimalFilter amount;

    private StringFilter label;

    private StringFilter identifier;

    private LongFilter bankAccountId;

    private LongFilter categoryId;

    private LongFilter subCategoryId;

    private LongFilter ownerId;

    private LongFilter classificationRuleId;

    public TransactionCriteria() {
    }

    public TransactionCriteria(TransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.bankAccountId = other.bankAccountId == null ? null : other.bankAccountId.copy();
        this.subCategoryId = other.subCategoryId == null ? null : other.subCategoryId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.classificationRuleId = other.classificationRuleId == null ? null : other.classificationRuleId.copy();
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public StringFilter getIdentifier() {
        return identifier;
    }

    public void setIdentifier(StringFilter identifier) {
        this.identifier = identifier;
    }

    public LongFilter getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(LongFilter bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(LongFilter subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getClassificationRuleId() {
        return classificationRuleId;
    }

    public void setClassificationRuleId(LongFilter classificationRuleId) {
        this.classificationRuleId = classificationRuleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionCriteria that = (TransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(label, that.label) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(bankAccountId, that.bankAccountId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(subCategoryId, that.subCategoryId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(classificationRuleId, that.classificationRuleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        amount,
        label,
        identifier,
        bankAccountId,
        categoryId,
        subCategoryId,
        ownerId,
        classificationRuleId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (identifier != null ? "identifier=" + identifier + ", " : "") +
                (bankAccountId != null ? "bankAccountId=" + bankAccountId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (subCategoryId != null ? "subCategoryId=" + subCategoryId + ", " : "") +
                (ownerId != null ? "bankAccount.owner=" + ownerId + ", " : "") +
                (classificationRuleId != null ? "classificationRuleId=" + classificationRuleId + ", " : "") +
            "}";
    }

}
