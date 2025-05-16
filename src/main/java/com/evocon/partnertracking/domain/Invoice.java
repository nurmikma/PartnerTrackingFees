package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal invoiceAmount;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @NotNull
    @Column(name = "invoice_type", nullable = false)
    private String invoiceType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "license", "invoice" }, allowSetters = true)
    private Set<InvoiceLine> lineItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "partner" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public Invoice invoiceAmount(BigDecimal invoiceAmount) {
        this.setInvoiceAmount(invoiceAmount);
        return this;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public Invoice invoiceType(String invoiceType) {
        this.setInvoiceType(invoiceType);
        return this;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Set<InvoiceLine> getLineItems() {
        return this.lineItems;
    }

    public void setLineItems(Set<InvoiceLine> invoiceLines) {
        if (this.lineItems != null) {
            this.lineItems.forEach(i -> i.setInvoice(null));
        }
        if (invoiceLines != null) {
            invoiceLines.forEach(i -> i.setInvoice(this));
        }
        this.lineItems = invoiceLines;
    }

    public Invoice lineItems(Set<InvoiceLine> invoiceLines) {
        this.setLineItems(invoiceLines);
        return this;
    }

    public Invoice addLineItems(InvoiceLine invoiceLine) {
        this.lineItems.add(invoiceLine);
        invoiceLine.setInvoice(this);
        return this;
    }

    public Invoice removeLineItems(InvoiceLine invoiceLine) {
        this.lineItems.remove(invoiceLine);
        invoiceLine.setInvoice(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Invoice client(Client client) {
        this.setClient(client);
        return this;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Invoice partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            "}";
    }

    @Transient
    public List<InvoiceLine> getActiveLineItems() {
        return this.lineItems.stream().filter(line -> isLicenseActive(line.getLicense())).collect(Collectors.toList());
    }

    private boolean isLicenseActive(License license) {
        if (license == null || this.invoiceDate == null) {
            return false;
        }
        LocalDate licenseStart = license.getLicenseStartDate();
        LocalDate licenseEnd = license.getLicenseEndDate();
        return (licenseStart != null && !invoiceDate.isBefore(licenseStart)) && (licenseEnd == null || !invoiceDate.isAfter(licenseEnd));
    }
    /*
    public Invoice(String clientId, String partnerId, BigDecimal invoiceAmount, LocalDate invoiceDate, String invoiceId) {
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.invoiceType = "license";
        this.lineItems = new HashSet<>();
    }
    */
}
