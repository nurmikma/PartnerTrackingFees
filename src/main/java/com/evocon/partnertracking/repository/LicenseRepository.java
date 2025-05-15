package com.evocon.partnertracking.repository;

import com.evocon.partnertracking.domain.License;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the License entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {}
