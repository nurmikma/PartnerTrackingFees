package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A InvoiceLine.
 */
@Entity
@Table(name = "invoice_line")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @JsonIgnoreProperties(value = { "client", "partner", "commissionRuleSet", "invoiceLine", "commissionFee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private License license;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lineItems", "client", "partner" }, allowSetters = true)
    private Invoice invoice;

    // Constructors
    public InvoiceLine() {}

    public InvoiceLine(License license) {
        this.license = license;
        this.totalAmount = license.getTotalLicenseAmount();
    }

    // Getters and setters
    public Long getId() {
        return this.id;
    }

    public InvoiceLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public InvoiceLine totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public License getLicense() {
        return this.license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public InvoiceLine license(License license) {
        this.setLicense(license);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceLine invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceLine)) return false;
        return getId() != null && getId().equals(((InvoiceLine) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        String licenseId = (license != null && license.getLicenseId() != null) ? license.getLicenseId() : "N/A";
        return String.format("InvoiceLine {id=%d, license='%s', totalAmount=%.2f}", id, licenseId, totalAmount);
    }
}
