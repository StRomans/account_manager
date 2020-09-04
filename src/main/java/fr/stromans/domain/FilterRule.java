package fr.stromans.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import fr.stromans.domain.enumeration.RuleField;

import fr.stromans.domain.enumeration.RuleOperator;

/**
 * A FilterRule.
 */
@Entity
@Table(name = "filter_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FilterRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "field", nullable = false)
    private RuleField field;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operator", nullable = false)
    private RuleOperator operator;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "number_value", precision = 21, scale = 2)
    private BigDecimal numberValue;

    @Column(name = "date_value")
    private LocalDate dateValue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "filterRules", allowSetters = true)
    private ClassificationRule classificationRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuleField getField() {
        return field;
    }

    public FilterRule field(RuleField field) {
        this.field = field;
        return this;
    }

    public void setField(RuleField field) {
        this.field = field;
    }

    public RuleOperator getOperator() {
        return operator;
    }

    public FilterRule operator(RuleOperator operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(RuleOperator operator) {
        this.operator = operator;
    }

    public String getStringValue() {
        return stringValue;
    }

    public FilterRule stringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public void setStringValue(String stringValue) {
        if(null != stringValue){
            stringValue = stringValue.trim().toUpperCase();
        }
        this.stringValue = stringValue;
    }

    public void setNumberValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
    }

    public BigDecimal getNumberValue() {
        return numberValue;
    }

    public FilterRule numberValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
        return this;
    }

    public LocalDate getDateValue() {
        return dateValue;
    }

    public FilterRule dateValue(LocalDate dateValue) {
        this.dateValue = dateValue;
        return this;
    }

    public void setDateValue(LocalDate dateValue) {
        this.dateValue = dateValue;
    }

    public ClassificationRule getClassificationRule() {
        return classificationRule;
    }

    public FilterRule classificationRule(ClassificationRule classificationRule) {
        this.classificationRule = classificationRule;
        return this;
    }

    public void setClassificationRule(ClassificationRule classificationRule) {
        this.classificationRule = classificationRule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilterRule)) {
            return false;
        }
        return id != null && id.equals(((FilterRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilterRule{" +
            "id=" + getId() +
            ", field='" + getField() + "'" +
            ", operator='" + getOperator() + "'" +
            ", stringValue='" + getStringValue() + "'" +
            "}";
    }

    public boolean isMatching(Transaction transaction){
        boolean isMatching = true;

        if(RuleField.AMOUNT.equals((this.getField()))){
            BigDecimal value = transaction.getAmount();
            if(RuleOperator.EQUALS.equals(this.getOperator()))              isMatching &= value.equals(this.getNumberValue());
            if(RuleOperator.LESSOREQUALTHAN.equals(this.getOperator()))     isMatching &= value.compareTo(this.getNumberValue()) <= 0;
            if(RuleOperator.GREATEROREQUALTHAN.equals(this.getOperator()))  isMatching &= value.compareTo(this.getNumberValue()) >= 0;
        }
        else if (RuleField.DATE.equals((this.getField()))){
            LocalDate value = transaction.getDate();
            if(RuleOperator.EQUALS.equals(this.getOperator()))              isMatching &= value.isEqual(this.getDateValue());
            if(RuleOperator.LESSOREQUALTHAN.equals(this.getOperator()))     isMatching &= value.compareTo(this.getDateValue()) <= 0;
            if(RuleOperator.GREATEROREQUALTHAN.equals(this.getOperator()))  isMatching &= value.compareTo(this.getDateValue()) >= 0;
        }
        else if (RuleField.LABEL.equals((this.getField()))){
            String value = transaction.getLabel();
            if(RuleOperator.EQUALS.equals(this.getOperator()))              isMatching &= value.equalsIgnoreCase(this.getStringValue());
            if(RuleOperator.CONTAINS.equals(this.getOperator()))            isMatching &= value.toUpperCase().contains(this.getStringValue().toUpperCase());
        }

        return isMatching;
    }
}
