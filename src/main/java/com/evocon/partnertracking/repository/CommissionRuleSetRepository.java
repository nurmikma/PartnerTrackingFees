package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.CommissionRuleSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommissionRuleSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionRuleSetRepository extends JpaRepository<CommissionRuleSet, Long> {}
