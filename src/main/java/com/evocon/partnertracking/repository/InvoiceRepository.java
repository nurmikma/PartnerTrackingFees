package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.Invoice;
import com.evocon.partnertracking.domain.Partner;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPartner(Partner partner);
}
