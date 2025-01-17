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

/**
 * Criteria class for the {@link fr.stromans.domain.ClassificationRule} entity. This class is used
 * in {@link fr.stromans.web.rest.ClassificationRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classification-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassificationRuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter priority;

    private BooleanFilter applyToUnclassified;

    private LongFilter ownerId;

    private LongFilter bankAccountId;

    private LongFilter transactionsId;

    private LongFilter filterRulesId;

    private LongFilter subCategoryId;

    public ClassificationRuleCriteria() {
    }

    public ClassificationRuleCriteria(ClassificationRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.applyToUnclassified = other.applyToUnclassified == null ? null : other.applyToUnclassified.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.bankAccountId = other.bankAccountId == null ? null : other.bankAccountId.copy();
        this.transactionsId = other.transactionsId == null ? null : other.transactionsId.copy();
        this.filterRulesId = other.filterRulesId == null ? null : other.filterRulesId.copy();
        this.subCategoryId = other.subCategoryId == null ? null : other.subCategoryId.copy();
    }

    @Override
    public ClassificationRuleCriteria copy() {
        return new ClassificationRuleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public BooleanFilter getApplyToUnclassified() {
        return applyToUnclassified;
    }

    public void setApplyToUnclassified(BooleanFilter applyToUnclassified) {
        this.applyToUnclassified = applyToUnclassified;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(LongFilter bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LongFilter getTransactionsId() {
        return transactionsId;
    }

    public void setTransactionsId(LongFilter transactionsId) {
        this.transactionsId = transactionsId;
    }

    public LongFilter getFilterRulesId() {
        return filterRulesId;
    }

    public void setFilterRulesId(LongFilter filterRulesId) {
        this.filterRulesId = filterRulesId;
    }

    public LongFilter getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(LongFilter subCategoryId) {
        this.subCategoryId = subCategoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassificationRuleCriteria that = (ClassificationRuleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(applyToUnclassified, that.applyToUnclassified) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(bankAccountId, that.bankAccountId) &&
            Objects.equals(transactionsId, that.transactionsId) &&
            Objects.equals(filterRulesId, that.filterRulesId) &&
            Objects.equals(subCategoryId, that.subCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        priority,
        applyToUnclassified,
        ownerId,
        bankAccountId,
        transactionsId,
        filterRulesId,
        subCategoryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationRuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (applyToUnclassified != null ? "applyToUnclassified=" + applyToUnclassified + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
                (bankAccountId != null ? "bankAccountId=" + bankAccountId + ", " : "") +
                (transactionsId != null ? "transactionsId=" + transactionsId + ", " : "") +
                (filterRulesId != null ? "filterRulesId=" + filterRulesId + ", " : "") +
                (subCategoryId != null ? "subCategoryId=" + subCategoryId + ", " : "") +
            "}";
    }

}
