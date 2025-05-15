package com.evocon.partnertracking.web.rest;

import com.evocon.partnertracking.domain.CommissionRuleSet;
import com.evocon.partnertracking.repository.CommissionRuleSetRepository;
import com.evocon.partnertracking.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.evocon.partnertracking.domain.CommissionRuleSet}.
 */
@RestController
@RequestMapping("/api/commission-rule-sets")
@Transactional
public class CommissionRuleSetResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommissionRuleSetResource.class);

    private static final String ENTITY_NAME = "commissionRuleSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommissionRuleSetRepository commissionRuleSetRepository;

    public CommissionRuleSetResource(CommissionRuleSetRepository commissionRuleSetRepository) {
        this.commissionRuleSetRepository = commissionRuleSetRepository;
    }

    /**
     * {@code POST  /commission-rule-sets} : Create a new commissionRuleSet.
     *
     * @param commissionRuleSet the commissionRuleSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commissionRuleSet, or with status {@code 400 (Bad Request)} if the commissionRuleSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommissionRuleSet> createCommissionRuleSet(@Valid @RequestBody CommissionRuleSet commissionRuleSet)
        throws URISyntaxException {
        LOG.debug("REST request to save CommissionRuleSet : {}", commissionRuleSet);
        if (commissionRuleSet.getId() != null) {
            throw new BadRequestAlertException("A new commissionRuleSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commissionRuleSet = commissionRuleSetRepository.save(commissionRuleSet);
        return ResponseEntity.created(new URI("/api/commission-rule-sets/" + commissionRuleSet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commissionRuleSet.getId().toString()))
            .body(commissionRuleSet);
    }

    /**
     * {@code PUT  /commission-rule-sets/:id} : Updates an existing commissionRuleSet.
     *
     * @param id the id of the commissionRuleSet to save.
     * @param commissionRuleSet the commissionRuleSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRuleSet,
     * or with status {@code 400 (Bad Request)} if the commissionRuleSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commissionRuleSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommissionRuleSet> updateCommissionRuleSet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommissionRuleSet commissionRuleSet
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommissionRuleSet : {}, {}", id, commissionRuleSet);
        if (commissionRuleSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRuleSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commissionRuleSet = commissionRuleSetRepository.save(commissionRuleSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionRuleSet.getId().toString()))
            .body(commissionRuleSet);
    }

    /**
     * {@code PATCH  /commission-rule-sets/:id} : Partial updates given fields of an existing commissionRuleSet, field will ignore if it is null
     *
     * @param id the id of the commissionRuleSet to save.
     * @param commissionRuleSet the commissionRuleSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRuleSet,
     * or with status {@code 400 (Bad Request)} if the commissionRuleSet is not valid,
     * or with status {@code 404 (Not Found)} if the commissionRuleSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the commissionRuleSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommissionRuleSet> partialUpdateCommissionRuleSet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommissionRuleSet commissionRuleSet
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommissionRuleSet partially : {}, {}", id, commissionRuleSet);
        if (commissionRuleSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRuleSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommissionRuleSet> result = commissionRuleSetRepository
            .findById(commissionRuleSet.getId())
            .map(existingCommissionRuleSet -> {
                if (commissionRuleSet.getRuleSetName() != null) {
                    existingCommissionRuleSet.setRuleSetName(commissionRuleSet.getRuleSetName());
                }

                return existingCommissionRuleSet;
            })
            .map(commissionRuleSetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionRuleSet.getId().toString())
        );
    }

    /**
     * {@code GET  /commission-rule-sets} : get all the commissionRuleSets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissionRuleSets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommissionRuleSet>> getAllCommissionRuleSets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CommissionRuleSets");
        Page<CommissionRuleSet> page = commissionRuleSetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commission-rule-sets/:id} : get the "id" commissionRuleSet.
     *
     * @param id the id of the commissionRuleSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commissionRuleSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommissionRuleSet> getCommissionRuleSet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommissionRuleSet : {}", id);
        Optional<CommissionRuleSet> commissionRuleSet = commissionRuleSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commissionRuleSet);
    }

    /**
     * {@code DELETE  /commission-rule-sets/:id} : delete the "id" commissionRuleSet.
     *
     * @param id the id of the commissionRuleSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommissionRuleSet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommissionRuleSet : {}", id);
        commissionRuleSetRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
