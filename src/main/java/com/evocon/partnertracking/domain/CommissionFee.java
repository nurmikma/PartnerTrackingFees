package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A CommissionFee.
 */
@Entity
@Table(name = "commission_fee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommissionFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "commission_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal commissionAmount;

    @JsonIgnoreProperties(value = { "client", "partner", "commissionRuleSet", "invoiceLine", "commissionFee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private License license;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CommissionFee() {}

    public CommissionFee(License license, BigDecimal commissionAmount) {
        this.license = license;
        this.commissionAmount = commissionAmount;
    }

    public Long getId() {
        return this.id;
    }

    public CommissionFee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCommissionAmount() {
        return this.commissionAmount;
    }

    public BigDecimal getCommissionFeeAmount() {
        return this.commissionAmount;
    }

    public CommissionFee commissionAmount(BigDecimal commissionAmount) {
        this.setCommissionAmount(commissionAmount);
        return this;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public License getLicense() {
        return this.license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public CommissionFee license(License license) {
        this.setLicense(license);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommissionFee)) {
            return false;
        }
        return getId() != null && getId().equals(((CommissionFee) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        String licenseId = license != null ? license.getLicenseId() : "null";
        return String.format("CommissionFee{id=%d, license=%s, commissionAmount=%.2f}", getId(), licenseId, commissionAmount);
    }
}
