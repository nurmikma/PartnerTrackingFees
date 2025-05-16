package com.evocon.partnertracking.domain;

import com.evocon.partnertracking.utils.Calculations;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
            ", ruleSetName='" + getRuleSetName() + "'" +
            "}";
    }

    public void addRule(CommissionRule newRule) {
        if (isOverlapping(newRule)) {
            throw new IllegalArgumentException("Commission rules cannot overlap.");
        }
        this.addRules(newRule);
    }

    private boolean isOverlapping(CommissionRule newRule) {
        for (CommissionRule rule : this.rules) {
            int ruleEndDay = rule.getEndDay() == null ? Integer.MAX_VALUE : rule.getEndDay();
            int newRuleEndDay = newRule.getEndDay() == null ? Integer.MAX_VALUE : newRule.getEndDay();

            if (newRule.getStartDay() <= ruleEndDay && rule.getStartDay() <= newRuleEndDay) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal calculateCommissionForLicense(
        LocalDate targetDate,
        BigDecimal licenseAmount,
        LocalDate licenseStart,
        LocalDate licenseEnd
    ) {
        LocalDate targetStartDate = targetDate.withDayOfMonth(1);
        LocalDate targetEndDate = targetStartDate.withDayOfMonth(targetStartDate.lengthOfMonth());

        int daysInMonth = targetStartDate.lengthOfMonth();
        BigDecimal dailyLicenseValue = Calculations.divide(licenseAmount, daysInMonth);
        BigDecimal totalCommission = BigDecimal.ZERO;

        for (LocalDate day : getApplicableDays(targetStartDate, targetEndDate, licenseStart, licenseEnd)) {
            long licenseAgeInDays = ChronoUnit.DAYS.between(licenseStart, day);
            CommissionRule rule = findApplicableRule(licenseAgeInDays);

            if (rule != null) {
                BigDecimal dailyCommission = Calculations.multiplyByPercentage(dailyLicenseValue, rule.getCommissionPercentage());
                totalCommission = Calculations.add(totalCommission, dailyCommission);
            }
        }
        return Calculations.roundToTwoDecimals(totalCommission);
    }

    private List<LocalDate> getApplicableDays(LocalDate targetStart, LocalDate targetEnd, LocalDate licenseStart, LocalDate licenseEnd) {
        List<LocalDate> applicableDays = new ArrayList<>();
        for (LocalDate day = targetStart; !day.isAfter(targetEnd); day = day.plusDays(1)) {
            if (!day.isBefore(licenseStart) && (licenseEnd == null || !day.isAfter(licenseEnd))) {
                applicableDays.add(day);
            }
        }
        return applicableDays;
    }

    private CommissionRule findApplicableRule(long licenseAgeInDays) {
        for (CommissionRule rule : this.rules) {
            long start = rule.getStartDay();
            long end = (rule.getEndDay() == null || rule.getEndDay() == 0) ? Long.MAX_VALUE : rule.getEndDay();
            if (licenseAgeInDays >= start && licenseAgeInDays <= end) {
                return rule;
            }
        }
        return null;
    }
}
