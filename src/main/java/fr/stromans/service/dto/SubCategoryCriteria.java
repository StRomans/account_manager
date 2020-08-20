package fr.stromans.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.stromans.domain.SubCategory} entity. This class is used
 * in {@link fr.stromans.web.rest.SubCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sub-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private LongFilter categoryId;

    private LongFilter ownerId;

    private StringFilter categoryLabel;

    public SubCategoryCriteria() {
    }

    public SubCategoryCriteria(SubCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.categoryLabel = other.categoryLabel == null ? null : other.categoryLabel.copy();
    }

    @Override
    public SubCategoryCriteria copy() {
        return new SubCategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public StringFilter getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(StringFilter categoryLabel) {
        this.categoryLabel = categoryLabel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubCategoryCriteria that = (SubCategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(categoryLabel, that.categoryLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        label,
        categoryId,
        ownerId,
        categoryLabel
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (ownerId != null ? "category.ownerId=" + ownerId + ", " : "") +
                (categoryLabel != null ? "category.label=" + categoryLabel + ", " : "") +
            "}";
    }

}
