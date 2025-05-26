package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.CommissionFee;
import com.evocon.partnertracking.domain.License;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommissionFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionFeeRepository extends JpaRepository<CommissionFee, Long> {
    @Query(
        """
        SELECT commissionFee
        FROM CommissionFee commissionFee
        WHERE commissionFee.license.id IN (
            SELECT invoiceLine.license.id
            FROM InvoiceLine invoiceLine
            WHERE invoiceLine IN (
                SELECT invoiceItem
                FROM Invoice invoice
                JOIN invoice.lineItems invoiceItem
                WHERE invoice.id = :invoiceId
            )
        )
        """
    )
    List<CommissionFee> findAllByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query(
        """
        SELECT commissionFee
        FROM CommissionFee commissionFee
        JOIN InvoiceLine invoiceLine ON invoiceLine.license = commissionFee.license
        JOIN Invoice invoice ON invoiceLine.invoice = invoice
        WHERE FUNCTION('YEAR', invoice.invoiceDate) = :year
          AND FUNCTION('MONTH', invoice.invoiceDate) = :month
        """
    )
    List<CommissionFee> findByInvoiceMonth(@Param("year") int year, @Param("month") int month);

    List<CommissionFee> findByLicense(License license);

    void deleteByLicense(License license);
}
