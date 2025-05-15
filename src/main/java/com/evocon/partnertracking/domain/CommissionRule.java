package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A CommissionRule.
 */
@Entity
@Table(name = "commission_rule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommissionRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "start_day", nullable = false)
    private Integer startDay;

    @Column(name = "end_day")
    private Integer endDay;

    @NotNull
    @Column(name = "commission_percentage", precision = 21, scale = 2, nullable = false)
    private BigDecimal commissionPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rules" }, allowSetters = true)
    private CommissionRuleSet commissionRuleSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CommissionRule() {}

    public CommissionRule(String ruleName, String description, int startDay, Integer endDay, BigDecimal commissionPercentage) {
        this.ruleName = ruleName;
        this.description = description;
        this.startDay = startDay;
        this.endDay = endDay;
        this.commissionPercentage = commissionPercentage;
    }

    public Long getId() {
        return this.id;
    }

    public CommissionRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public CommissionRule ruleName(String ruleName) {
        this.setRuleName(ruleName);
        return this;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDescription() {
        return this.description;
    }

    public CommissionRule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStartDay() {
        return this.startDay;
    }

    public CommissionRule startDay(Integer startDay) {
        this.setStartDay(startDay);
        return this;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public Integer getEndDay() {
        return this.endDay;
    }

    public CommissionRule endDay(Integer endDay) {
        this.setEndDay(endDay);
        return this;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public BigDecimal getCommissionPercentage() {
        return this.commissionPercentage;
    }

    public CommissionRule commissionPercentage(BigDecimal commissionPercentage) {
        this.setCommissionPercentage(commissionPercentage);
        return this;
    }

    public void setCommissionPercentage(BigDecimal commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public CommissionRuleSet getCommissionRuleSet() {
        return this.commissionRuleSet;
    }

    public void setCommissionRuleSet(CommissionRuleSet commissionRuleSet) {
        this.commissionRuleSet = commissionRuleSet;
    }

    public CommissionRule commissionRuleSet(CommissionRuleSet commissionRuleSet) {
        this.setCommissionRuleSet(commissionRuleSet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommissionRule)) {
            return false;
        }
        return getId() != null && getId().equals(((CommissionRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommissionRule{" +
            "id=" + getId() +
            ", ruleName='" + getRuleName() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDay=" + getStartDay() +
            ", endDay=" + getEndDay() +
            ", commissionPercentage=" + getCommissionPercentage() +
            "}";
    }
}
