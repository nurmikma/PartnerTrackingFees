package com.evocon.partnertracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
    @Column(name = "invoice_id", nullable = false)
    private String invoiceId;

    @NotNull
    @Column(name = "invoice_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal invoiceAmount;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @NotNull
    @Column(name = "invoice_type", nullable = false)
    private String invoiceType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "license", "invoice" }, allowSetters = true)
    private Set<InvoiceLine> lineItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "partner" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    // Constructors
    public Invoice() {
        this.invoiceType = "license";
        this.lineItems = new HashSet<>();
    }

    public Invoice(String invoiceId, BigDecimal invoiceAmount, LocalDate invoiceDate, String invoiceType, Client client, Partner partner) {
        this.invoiceId = invoiceId;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.invoiceType = invoiceType;
        this.client = client;
        this.partner = partner;
        this.lineItems = new HashSet<>();
    }

    // Business logic method: Add InvoiceLine from License
    public void addLineItem(License license) {
        InvoiceLine line = new InvoiceLine(license);
        this.lineItems.add(line);
        line.setInvoice(this);
    }

    // Business logic method: Get only active line items
    @JsonIgnore
    public Set<InvoiceLine> getActiveLineItems() {
        return this.lineItems.stream().filter(line -> isLicenseActive(line.getLicense())).collect(Collectors.toSet());
    }

    private boolean isLicenseActive(License license) {
        LocalDate licenseStart = license.getLicenseStartDate();
        LocalDate licenseEnd = license.getLicenseEndDate();
        return !this.invoiceDate.isBefore(licenseStart) && (licenseEnd == null || !this.invoiceDate.isAfter(licenseEnd));
    }

    // Getters and setters
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

    public String getInvoiceId() {
        return this.invoiceId;
    }

    public Invoice invoiceId(String invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    // Equality and hash
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // toString
    @Override
    public String toString() {
        return (
            "Invoice{" +
            "id=" +
            getId() +
            ", invoiceId='" +
            getInvoiceId() +
            "'" +
            ", invoiceAmount=" +
            getInvoiceAmount() +
            ", invoiceDate='" +
            getInvoiceDate() +
            "'" +
            ", invoiceType='" +
            getInvoiceType() +
            "'" +
            ", clientId='" +
            (client != null ? client.getId() : "null") +
            "'" + // Accessing clientId via the client entity
            ", partnerId='" +
            (partner != null ? partner.getId() : "null") +
            "'" + // Accessing partnerId via the partner entity
            "}"
        );
    }
}
