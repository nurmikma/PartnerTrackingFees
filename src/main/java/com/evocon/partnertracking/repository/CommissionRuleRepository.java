package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.CommissionRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommissionRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionRuleRepository extends JpaRepository<CommissionRule, Long> {}
