package com.evocon.partnertracking.web.rest;

import com.evocon.partnertracking.domain.CommissionFee;
import com.evocon.partnertracking.domain.CommissionRuleSet;
import com.evocon.partnertracking.domain.Invoice;
import com.evocon.partnertracking.repository.CommissionFeeRepository;
import com.evocon.partnertracking.repository.CommissionRuleSetRepository;
import com.evocon.partnertracking.repository.InvoiceRepository;
import com.evocon.partnertracking.service.CommissionService;
import com.evocon.partnertracking.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.evocon.partnertracking.domain.CommissionFee}.
 */
@RestController
@RequestMapping("/api/commission-fees")
@Transactional
public class CommissionFeeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommissionFeeResource.class);

    private static final String ENTITY_NAME = "commissionFee";
    private final CommissionRuleSetRepository commissionRuleSetRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommissionFeeRepository commissionFeeRepository;
    private final InvoiceRepository invoiceRepository;
    private final CommissionService commissionService;

    public CommissionFeeResource(
        CommissionFeeRepository commissionFeeRepository,
        InvoiceRepository invoiceRepository,
        CommissionService commissionService,
        CommissionRuleSetRepository commissionRuleSetRepository
    ) {
        this.commissionFeeRepository = commissionFeeRepository;
        this.invoiceRepository = invoiceRepository;
        this.commissionService = commissionService;
        this.commissionRuleSetRepository = commissionRuleSetRepository;
    }

    /**
     * {@code POST  /commission-fees} : Create a new commissionFee.
     *
     * @param commissionFee the commissionFee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commissionFee, or with status {@code 400 (Bad Request)} if the commissionFee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommissionFee> createCommissionFee(@Valid @RequestBody CommissionFee commissionFee) throws URISyntaxException {
        LOG.debug("REST request to save CommissionFee : {}", commissionFee);
        if (commissionFee.getId() != null) {
            throw new BadRequestAlertException("A new commissionFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commissionFee = commissionFeeRepository.save(commissionFee);
        return ResponseEntity.created(new URI("/api/commission-fees/" + commissionFee.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commissionFee.getId().toString()))
            .body(commissionFee);
    }

    /**
     * {@code PUT  /commission-fees/:id} : Updates an existing commissionFee.
     *
     * @param id the id of the commissionFee to save.
     * @param commissionFee the commissionFee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionFee,
     * or with status {@code 400 (Bad Request)} if the commissionFee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commissionFee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommissionFee> updateCommissionFee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommissionFee commissionFee
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommissionFee : {}, {}", id, commissionFee);
        if (commissionFee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionFee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionFeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commissionFee = commissionFeeRepository.save(commissionFee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionFee.getId().toString()))
            .body(commissionFee);
    }

    /**
     * {@code PATCH  /commission-fees/:id} : Partial updates given fields of an existing commissionFee, field will ignore if it is null
     *
     * @param id the id of the commissionFee to save.
     * @param commissionFee the commissionFee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionFee,
     * or with status {@code 400 (Bad Request)} if the commissionFee is not valid,
     * or with status {@code 404 (Not Found)} if the commissionFee is not found,
     * or with status {@code 500 (Internal Server Error)} if the commissionFee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommissionFee> partialUpdateCommissionFee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommissionFee commissionFee
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommissionFee partially : {}, {}", id, commissionFee);
        if (commissionFee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionFee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionFeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommissionFee> result = commissionFeeRepository
            .findById(commissionFee.getId())
            .map(existingCommissionFee -> {
                if (commissionFee.getCommissionAmount() != null) {
                    existingCommissionFee.setCommissionAmount(commissionFee.getCommissionAmount());
                }

                return existingCommissionFee;
            })
            .map(commissionFeeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commissionFee.getId().toString())
        );
    }

    /**
     * {@code GET  /commission-fees} : get all the commissionFees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissionFees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommissionFee>> getAllCommissionFees(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CommissionFees");
        Page<CommissionFee> page = commissionFeeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commission-fees/:id} : get the "id" commissionFee.
     *
     * @param id the id of the commissionFee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commissionFee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommissionFee> getCommissionFee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommissionFee : {}", id);
        Optional<CommissionFee> commissionFee = commissionFeeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commissionFee);
    }

    /**
     * {@code DELETE  /commission-fees/:id} : delete the "id" commissionFee.
     *
     * @param id the id of the commissionFee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommissionFee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommissionFee : {}", id);
        commissionFeeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<Map<String, Object>>> getInvoiceOptions() {
        List<Map<String, Object>> options = invoiceRepository
            .findAll()
            .stream()
            .map(invoice -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", invoice.getId());
                map.put("name", "Invoice #" + invoice.getId());
                return map;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(options);
    }

    @PostMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<CommissionFee>> generateCommissionFeesByInvoice(@PathVariable Long invoiceId) {
        LOG.debug("REST request to dynamically calculate CommissionFees for Invoice ID: {}", invoiceId);

        Invoice invoice = invoiceRepository
            .findById(invoiceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        List<CommissionRuleSet> ruleSets = commissionRuleSetRepository.findAll();

        // Make sure your service saves the fees, not just calculates
        List<CommissionFee> calculatedFees = commissionService.calculateCommissionFeesForInvoice(invoice, ruleSets);

        // Save the calculated fees in DB (make sure this is implemented in your service)
        commissionFeeRepository.saveAll(calculatedFees);

        return ResponseEntity.ok(calculatedFees);
    }
}
