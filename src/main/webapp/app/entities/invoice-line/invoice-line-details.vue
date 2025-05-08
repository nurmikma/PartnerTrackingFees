<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="invoiceLine">
        <h2 class="jh-entity-heading" data-cy="invoiceLineDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.invoiceLine.detail.title')"></span> {{ invoiceLine.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.invoiceLine.totalAmount')"></span>
          </dt>
          <dd>
            <span>{{ invoiceLine.totalAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.invoiceLine.license')"></span>
          </dt>
          <dd>
            <div v-if="invoiceLine.license">
              <router-link :to="{ name: 'LicenseView', params: { licenseId: invoiceLine.license.id } }">{{
                invoiceLine.license.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.invoiceLine.invoice')"></span>
          </dt>
          <dd>
            <div v-if="invoiceLine.invoice">
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoiceLine.invoice.id } }">{{
                invoiceLine.invoice.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="invoiceLine.id"
          :to="{ name: 'InvoiceLineEdit', params: { invoiceLineId: invoiceLine.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice-line-details.component.ts"></script>
