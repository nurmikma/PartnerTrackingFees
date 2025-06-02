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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_line_id")
    @JsonIgnoreProperties(value = { "invoice", "license", "commissionFees" }, allowSetters = true)
    private InvoiceLine invoiceLine;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CommissionFee() {}

    public CommissionFee(BigDecimal commissionAmount, Invoice invoice, InvoiceLine invoiceLine) {
        this.commissionAmount = commissionAmount;
        this.invoice = invoice;
        this.invoiceLine = invoiceLine;
    }

    public CommissionFee(BigDecimal commissionAmount, InvoiceLine invoiceLine) {
        this.commissionAmount = commissionAmount;
        this.invoiceLine = invoiceLine;
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

    public InvoiceLine getInvoiceLine() {
        return invoiceLine;
    }

    public void setInvoiceLine(InvoiceLine invoiceLine) {
        this.invoiceLine = invoiceLine;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommissionFee{" +
            "id=" + getId() +
            ", commissionAmount=" + getCommissionAmount() +
            "}";
    }
}
