package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.CommissionFee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommissionFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionFeeRepository extends JpaRepository<CommissionFee, Long> {}
