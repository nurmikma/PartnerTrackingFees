package com.evocon.partnertracking.domain;

import com.evocon.partnertracking.utils.Calculations;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A License.
 */
@Entity
@Table(name = "license")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class License implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "license_id", nullable = false)
    private String licenseId;

    @NotNull
    @Column(name = "license_rule_name", nullable = false)
    private String licenseRuleName;

    @NotNull
    @Column(name = "license_start_date", nullable = false)
    private LocalDate licenseStartDate;

    @Column(name = "license_end_date")
    private LocalDate licenseEndDate;

    @NotNull
    @Column(name = "license_quantity", nullable = false)
    private Integer licenseQuantity;

    @NotNull
    @Column(name = "price_per_license", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerLicense;

    @NotNull
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @NotNull
    @Column(name = "partner_id", nullable = false)
    private String partnerId;

    @NotNull
    @Column(name = "commission_rule_set_id", nullable = false)
    private String commissionRuleSetId;

    @Column(name = "total_license_amount", precision = 21, scale = 2)
    private BigDecimal totalLicenseAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "partner" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rules" }, allowSetters = true)
    private CommissionRuleSet commissionRuleSet;

    @JsonIgnoreProperties(value = { "license", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "license")
    private InvoiceLine invoiceLine;

    @JsonIgnoreProperties(value = { "license" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "license")
    private CommissionFee commissionFee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    // Default constructor for JSON
    public License() {}

    public License(
        String licenseId,
        String licenseRuleName,
        LocalDate licenseStartDate,
        LocalDate licenseEndDate,
        Integer licenseQuantity,
        BigDecimal pricePerLicense,
        String clientId,
        String partnerId,
        String commissionRuleSetId
    ) {
        this.licenseId = licenseId;
        this.licenseRuleName = licenseRuleName;
        this.licenseStartDate = licenseStartDate;
        this.licenseEndDate = licenseEndDate;
        this.licenseQuantity = licenseQuantity;
        this.pricePerLicense = pricePerLicense;
        this.clientId = clientId;
        this.partnerId = partnerId;
        this.commissionRuleSetId = commissionRuleSetId;
        this.totalLicenseAmount = Calculations.calculateTotalLicenseAmount(this.licenseQuantity, this.pricePerLicense);
    }

    @Override
    public String toString() {
        return (
            "License{" +
            "licenseId='" +
            licenseId +
            '\'' +
            ", licenseRuleName='" +
            licenseRuleName +
            '\'' +
            ", licenseStartDate=" +
            licenseStartDate +
            ", licenseQuantity=" +
            licenseQuantity +
            ", pricePerLicense=" +
            pricePerLicense +
            ", clientId='" +
            clientId +
            '\'' +
            ", partnerId='" +
            partnerId +
            '\'' +
            ", commissionRuleSet=" +
            commissionRuleSetId +
            ", totalLicenseAmount=" +
            totalLicenseAmount +
            '}'
        );
    }

    public boolean isActiveForMonth(LocalDate targetMonth) {
        return licenseEndDate == null || !licenseEndDate.isBefore(targetMonth);
    }

    public Long getId() {
        return this.id;
    }

    public License id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseId() {
        return this.licenseId;
    }

    public License licenseId(String licenseId) {
        this.setLicenseId(licenseId);
        return this;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseRuleName() {
        return this.licenseRuleName;
    }

    public License licenseRuleName(String licenseRuleName) {
        this.setLicenseRuleName(licenseRuleName);
        return this;
    }

    public void setLicenseRuleName(String licenseRuleName) {
        this.licenseRuleName = licenseRuleName;
    }

    public LocalDate getLicenseStartDate() {
        return this.licenseStartDate;
    }

    public License licenseStartDate(LocalDate licenseStartDate) {
        this.setLicenseStartDate(licenseStartDate);
        return this;
    }

    public void setLicenseStartDate(LocalDate licenseStartDate) {
        this.licenseStartDate = licenseStartDate;
    }

    public LocalDate getLicenseEndDate() {
        return this.licenseEndDate;
    }

    public License licenseEndDate(LocalDate licenseEndDate) {
        this.setLicenseEndDate(licenseEndDate);
        return this;
    }

    public void setLicenseEndDate(LocalDate licenseEndDate) {
        this.licenseEndDate = licenseEndDate;
    }

    public Integer getLicenseQuantity() {
        return this.licenseQuantity;
    }

    public License licenseQuantity(Integer licenseQuantity) {
        this.setLicenseQuantity(licenseQuantity);
        return this;
    }

    public void setLicenseQuantity(Integer licenseQuantity) {
        this.licenseQuantity = licenseQuantity;
        recalculateTotalAmount();
    }

    public BigDecimal getPricePerLicense() {
        return this.pricePerLicense;
    }

    public License pricePerLicense(BigDecimal pricePerLicense) {
        this.setPricePerLicense(pricePerLicense);
        return this;
    }

    public void setPricePerLicense(BigDecimal pricePerLicense) {
        this.pricePerLicense = pricePerLicense;
        recalculateTotalAmount();
    }

    public String getClientId() {
        return this.clientId;
    }

    public License clientId(String clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public License partnerId(String partnerId) {
        this.setPartnerId(partnerId);
        return this;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getCommissionRuleSetId() {
        return this.commissionRuleSetId;
    }

    public License commissionRuleSetId(String commissionRuleSetId) {
        this.setCommissionRuleSetId(commissionRuleSetId);
        return this;
    }

    public void setCommissionRuleSetId(String commissionRuleSetId) {
        this.commissionRuleSetId = commissionRuleSetId;
    }

    public BigDecimal getTotalLicenseAmount() {
        return this.totalLicenseAmount;
    }

    public License totalLicenseAmount(BigDecimal totalLicenseAmount) {
        this.setTotalLicenseAmount(totalLicenseAmount);
        return this;
    }

    public void setTotalLicenseAmount(BigDecimal totalLicenseAmount) {
        this.totalLicenseAmount = totalLicenseAmount;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public License client(Client client) {
        this.setClient(client);
        return this;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public License partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public CommissionRuleSet getCommissionRuleSet() {
        return this.commissionRuleSet;
    }

    public void setCommissionRuleSet(CommissionRuleSet commissionRuleSet) {
        this.commissionRuleSet = commissionRuleSet;
    }

    public License commissionRuleSet(CommissionRuleSet commissionRuleSet) {
        this.setCommissionRuleSet(commissionRuleSet);
        return this;
    }

    public InvoiceLine getInvoiceLine() {
        return this.invoiceLine;
    }

    public void setInvoiceLine(InvoiceLine invoiceLine) {
        if (this.invoiceLine != null) {
            this.invoiceLine.setLicense(null);
        }
        if (invoiceLine != null) {
            invoiceLine.setLicense(this);
        }
        this.invoiceLine = invoiceLine;
    }

    public License invoiceLine(InvoiceLine invoiceLine) {
        this.setInvoiceLine(invoiceLine);
        return this;
    }

    public CommissionFee getCommissionFee() {
        return this.commissionFee;
    }

    public void setCommissionFee(CommissionFee commissionFee) {
        if (this.commissionFee != null) {
            this.commissionFee.setLicense(null);
        }
        if (commissionFee != null) {
            commissionFee.setLicense(this);
        }
        this.commissionFee = commissionFee;
    }

    public License commissionFee(CommissionFee commissionFee) {
        this.setCommissionFee(commissionFee);
        return this;
    }

    // Private method to recalculate total license amount
    private void recalculateTotalAmount() {
        if (pricePerLicense != null && licenseQuantity != null && licenseQuantity > 0) {
            this.totalLicenseAmount = Calculations.calculateTotalLicenseAmount(this.licenseQuantity, this.pricePerLicense);
        }
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof License)) {
            return false;
        }
        return getId() != null && getId().equals(((License) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
