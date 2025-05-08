<template>
  <div>
    <h2 id="page-heading" data-cy="InvoiceHeading">
      <span v-text="t$('partnerTrackingFeesApp.invoice.home.title')" id="invoice-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('partnerTrackingFeesApp.invoice.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'InvoiceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-invoice"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('partnerTrackingFeesApp.invoice.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && invoices && invoices.length === 0">
      <span v-text="t$('partnerTrackingFeesApp.invoice.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="invoices && invoices.length > 0">
      <table class="table table-striped" aria-describedby="invoices">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('invoiceId')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.invoiceId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('clientId')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.clientId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'clientId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('partnerId')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.partnerId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'partnerId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('invoiceAmount')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.invoiceAmount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('invoiceDate')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.invoiceDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('invoiceType')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.invoiceType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('client.id')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.client')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'client.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('partner.id')">
              <span v-text="t$('partnerTrackingFeesApp.invoice.partner')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'partner.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="invoice in invoices" :key="invoice.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }">{{ invoice.id }}</router-link>
            </td>
            <td>{{ invoice.invoiceId }}</td>
            <td>{{ invoice.clientId }}</td>
            <td>{{ invoice.partnerId }}</td>
            <td>{{ invoice.invoiceAmount }}</td>
            <td>{{ invoice.invoiceDate }}</td>
            <td>{{ invoice.invoiceType }}</td>
            <td>
              <div v-if="invoice.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: invoice.client.id } }">{{ invoice.client.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="invoice.partner">
                <router-link :to="{ name: 'PartnerView', params: { partnerId: invoice.partner.id } }">{{ invoice.partner.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'InvoiceEdit', params: { invoiceId: invoice.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(invoice)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="partnerTrackingFeesApp.invoice.delete.question"
          data-cy="invoiceDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-invoice-heading" v-text="t$('partnerTrackingFeesApp.invoice.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-invoice"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeInvoice()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="invoices && invoices.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice.component.ts"></script>
