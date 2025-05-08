package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CommissionRuleSet.
 */
@Entity
@Table(name = "commission_rule_set")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommissionRuleSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "commission_rule_set_id", nullable = false)
    private String commissionRuleSetId;

    @NotNull
    @Column(name = "rule_set_name", nullable = false)
    private String ruleSetName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commissionRuleSet")
    @JsonIgnoreProperties(value = { "commissionRuleSet" }, allowSetters = true)
    private Set<CommissionRule> rules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommissionRuleSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommissionRuleSetId() {
        return this.commissionRuleSetId;
    }

    public CommissionRuleSet commissionRuleSetId(String commissionRuleSetId) {
        this.setCommissionRuleSetId(commissionRuleSetId);
        return this;
    }

    public void setCommissionRuleSetId(String commissionRuleSetId) {
        this.commissionRuleSetId = commissionRuleSetId;
    }

    public String getRuleSetName() {
        return this.ruleSetName;
    }

    public CommissionRuleSet ruleSetName(String ruleSetName) {
        this.setRuleSetName(ruleSetName);
        return this;
    }

    public void setRuleSetName(String ruleSetName) {
        this.ruleSetName = ruleSetName;
    }

    public Set<CommissionRule> getRules() {
        return this.rules;
    }

    public void setRules(Set<CommissionRule> commissionRules) {
        if (this.rules != null) {
            this.rules.forEach(i -> i.setCommissionRuleSet(null));
        }
        if (commissionRules != null) {
            commissionRules.forEach(i -> i.setCommissionRuleSet(this));
        }
        this.rules = commissionRules;
    }

    public CommissionRuleSet rules(Set<CommissionRule> commissionRules) {
        this.setRules(commissionRules);
        return this;
    }

    public CommissionRuleSet addRules(CommissionRule commissionRule) {
        this.rules.add(commissionRule);
        commissionRule.setCommissionRuleSet(this);
        return this;
    }

    public CommissionRuleSet removeRules(CommissionRule commissionRule) {
        this.rules.remove(commissionRule);
        commissionRule.setCommissionRuleSet(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommissionRuleSet)) {
            return false;
        }
        return getId() != null && getId().equals(((CommissionRuleSet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommissionRuleSet{" +
            "id=" + getId() +
            ", commissionRuleSetId='" + getCommissionRuleSetId() + "'" +
            ", ruleSetName='" + getRuleSetName() + "'" +
            "}";
    }
}
