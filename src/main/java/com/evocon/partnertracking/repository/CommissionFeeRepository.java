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
            SELECT cf
            FROM CommissionFee cf
            WHERE cf.license.id IN (
                SELECT il.license.id
                FROM InvoiceLine il
                WHERE il IN (
                    SELECT invLine
                    FROM Invoice inv
                    JOIN inv.lineItems invLine
                    WHERE inv.id = :invoiceId
                )
              )
        """
    )
    List<CommissionFee> findAllByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query(
        """
            SELECT cf FROM CommissionFee cf
            JOIN InvoiceLine il ON il.license = cf.license
            JOIN Invoice i ON il.invoice = i
            WHERE FUNCTION('YEAR', i.invoiceDate) = :year AND FUNCTION('MONTH', i.invoiceDate) = :month
        """
    )
    List<CommissionFee> findByInvoiceMonth(@Param("year") int year, @Param("month") int month);

    List<CommissionFee> findByLicense(License license);
    void deleteByLicense(License license);
}
