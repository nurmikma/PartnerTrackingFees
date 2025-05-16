package com.evocon.partnertracking.service;

import com.evocon.partnertracking.domain.*;
import com.evocon.partnertracking.repository.CommissionRuleSetRepository;
import com.evocon.partnertracking.repository.InvoiceRepository;
import com.evocon.partnertracking.repository.PartnerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommissionService {

    private static final Logger logger = LoggerFactory.getLogger(CommissionService.class);

    private final InvoiceRepository invoiceRepo;
    private final CommissionRuleSetRepository ruleSetRepo;
    private final PartnerRepository partnerRepo;

    public CommissionService(InvoiceRepository invoiceRepo, CommissionRuleSetRepository ruleSetRepo, PartnerRepository partnerRepo) {
        this.invoiceRepo = invoiceRepo;
        this.ruleSetRepo = ruleSetRepo;
        this.partnerRepo = partnerRepo;
    }

    public List<CommissionResult> calculateMonthlyCommissionsForPartner(Long partnerId, LocalDate currentDate) {
        Partner partner = partnerRepo
            .findById(partnerId)
            .orElseThrow(() -> new IllegalArgumentException("Partner not found: " + partnerId));

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        List<Invoice> invoices = invoiceRepo.findByPartner(partner);

        List<Invoice> targetMonthInvoices = invoices
            .stream()
            .filter(invoice -> !invoice.getInvoiceDate().isBefore(firstDayOfMonth) && !invoice.getInvoiceDate().isAfter(lastDayOfMonth))
            .toList();

        List<CommissionRuleSet> ruleSets = ruleSetRepo.findAll();

        List<CommissionResult> results = new ArrayList<>();
        for (Invoice invoice : targetMonthInvoices) {
            List<CommissionFee> commissions = calculateCommissionsForInvoice(invoice, ruleSets);
            results.add(new CommissionResult(invoice, commissions));
        }

        return results;
    }

    public void calculateAndLogCommissionsForPartner(String partnerId) {
        List<Invoice> allInvoices = invoiceRepo.findAll();
        List<CommissionRuleSet> ruleSets = ruleSetRepo.findAll();

        BigDecimal totalPartnerCommission = BigDecimal.ZERO;

        for (Invoice invoice : allInvoices) {
            if (!invoice.getPartner().getId().toString().equals(partnerId)) {
                continue;
            }

            List<CommissionFee> commissions = calculateCommissionsForInvoice(invoice, ruleSets);
            BigDecimal invoiceTotal = commissions
                .stream()
                .map(CommissionFee::getCommissionFeeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalPartnerCommission = totalPartnerCommission.add(invoiceTotal);

            logger.info("Invoice ID: {}, Client: {}", invoice.getId(), invoice.getClient().getId());
            logger.info("Total Commission: {}", invoiceTotal);
            for (CommissionFee fee : commissions) {
                logger.debug(" - {}", fee);
            }
        }

        logger.info("=== TOTAL COMMISSION FOR PARTNER {}: {} ===", partnerId, totalPartnerCommission);
    }

    private List<CommissionFee> calculateCommissionsForInvoice(Invoice invoice, List<CommissionRuleSet> commissionRuleSets) {
        List<CommissionFee> commissions = new ArrayList<>();
        // Calculate for the previous month of invoice date
        LocalDate targetMonth = invoice.getInvoiceDate().minusMonths(1).withDayOfMonth(1);

        for (InvoiceLine lineItem : invoice.getLineItems()) {
            License license = lineItem.getLicense();

            if (license.isActiveForMonth(targetMonth)) {
                Optional<CommissionRuleSet> optionalCommissionRuleSet = commissionRuleSets
                    .stream()
                    .filter(ruleSet -> ruleSet.getId().equals(license.getCommissionRuleSet().getId()))
                    .findFirst();

                CommissionRuleSet commissionRuleSet = optionalCommissionRuleSet.orElseThrow(() ->
                    new IllegalArgumentException("Commission Ruleset not found for license: " + license.getId())
                );

                BigDecimal commissionAmount = commissionRuleSet.calculateCommissionForLicense(
                    targetMonth,
                    license.getTotalLicenseAmount(),
                    license.getLicenseStartDate(),
                    license.getLicenseEndDate()
                );

                commissions.add(new CommissionFee(license, commissionAmount));
            }
        }

        return commissions;
    }

    // Helper DTO class to hold invoice and its commissions
    public static class CommissionResult {

        private final Invoice invoice;
        private final List<CommissionFee> commissions;

        public CommissionResult(Invoice invoice, List<CommissionFee> commissions) {
            this.invoice = invoice;
            this.commissions = commissions;
        }

        public Invoice getInvoice() {
            return invoice;
        }

        public List<CommissionFee> getCommissions() {
            return commissions;
        }

        public BigDecimal getTotalCommission() {
            return commissions.stream().map(CommissionFee::getCommissionFeeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
