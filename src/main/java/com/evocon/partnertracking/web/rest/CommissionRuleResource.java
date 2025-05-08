package com.evocon.partnertracking.web.rest;

import com.evocon.partnertracking.domain.CommissionRule;
import com.evocon.partnertracking.repository.CommissionRuleRepository;
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
 * REST controller for managing {@link com.evocon.partnertracking.domain.CommissionRule}.
 */
@RestController
@RequestMapping("/api/commission-rules")
@Transactional
public class CommissionRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommissionRuleResource.class);

    private static final String ENTITY_NAME = "commissionRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommissionRuleRepository commissionRuleRepository;

    public CommissionRuleResource(CommissionRuleRepository commissionRuleRepository) {
        this.commissionRuleRepository = commissionRuleRepository;
    }

    /**
     * {@code POST  /commission-rules} : Create a new commissionRule.
     *
     * @param commissionRule the commissionRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commissionRule, or with status {@code 400 (Bad Request)} if the commissionRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommissionRule> createCommissionRule(@Valid @RequestBody CommissionRule commissionRule)
        throws URISyntaxException {
        LOG.debug("REST request to save CommissionRule : {}", commissionRule);
        if (commissionRule.getId() != null) {
            throw new BadRequestAlertException("A new commissionRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commissionRule = commissionRuleRepository.save(commissionRule);
        return ResponseEntity.created(new URI("/api/commission-rules/" + commissionRule.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commissionRule.getId().toString()))
            .body(commissionRule);
    }

    /**
     * {@code PUT  /commission-rules/:id} : Updates an existing commissionRule.
     *
     * @param id the id of the commissionRule to save.
     * @param commissionRule the commissionRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRule,
     * or with status {@code 400 (Bad Request)} if the commissionRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commissionRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommissionRule> updateCommissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommissionRule commissionRule
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommissionRule : {}, {}", id, commissionRule);
        if (commissionRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commissionRule = commissionRuleRepository.save(commissionRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionRule.getId().toString()))
            .body(commissionRule);
    }

    /**
     * {@code PATCH  /commission-rules/:id} : Partial updates given fields of an existing commissionRule, field will ignore if it is null
     *
     * @param id the id of the commissionRule to save.
     * @param commissionRule the commissionRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRule,
     * or with status {@code 400 (Bad Request)} if the commissionRule is not valid,
     * or with status {@code 404 (Not Found)} if the commissionRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the commissionRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommissionRule> partialUpdateCommissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommissionRule commissionRule
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommissionRule partially : {}, {}", id, commissionRule);
        if (commissionRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommissionRule> result = commissionRuleRepository
            .findById(commissionRule.getId())
            .map(existingCommissionRule -> {
                if (commissionRule.getRuleName() != null) {
                    existingCommissionRule.setRuleName(commissionRule.getRuleName());
                }
                if (commissionRule.getDescription() != null) {
                    existingCommissionRule.setDescription(commissionRule.getDescription());
                }
                if (commissionRule.getStartDay() != null) {
                    existingCommissionRule.setStartDay(commissionRule.getStartDay());
                }
                if (commissionRule.getEndDay() != null) {
                    existingCommissionRule.setEndDay(commissionRule.getEndDay());
                }
                if (commissionRule.getCommissionPercentage() != null) {
                    existingCommissionRule.setCommissionPercentage(commissionRule.getCommissionPercentage());
                }

                return existingCommissionRule;
            })
            .map(commissionRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionRule.getId().toString())
        );
    }

    /**
     * {@code GET  /commission-rules} : get all the commissionRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissionRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommissionRule>> getAllCommissionRules(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CommissionRules");
        Page<CommissionRule> page = commissionRuleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commission-rules/:id} : get the "id" commissionRule.
     *
     * @param id the id of the commissionRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commissionRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommissionRule> getCommissionRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommissionRule : {}", id);
        Optional<CommissionRule> commissionRule = commissionRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commissionRule);
    }

    /**
     * {@code DELETE  /commission-rules/:id} : delete the "id" commissionRule.
     *
     * @param id the id of the commissionRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommissionRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommissionRule : {}", id);
        commissionRuleRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
