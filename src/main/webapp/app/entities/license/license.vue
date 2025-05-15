<template>
  <div>
    <h2 id="page-heading" data-cy="LicenseHeading">
      <span v-text="t$('partnerTrackingFeesApp.license.home.title')" id="license-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('partnerTrackingFeesApp.license.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'LicenseCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-license"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('partnerTrackingFeesApp.license.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && licenses && licenses.length === 0">
      <span v-text="t$('partnerTrackingFeesApp.license.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="licenses && licenses.length > 0">
      <table class="table table-striped" aria-describedby="licenses">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('licenseRuleName')">
              <span v-text="t$('partnerTrackingFeesApp.license.licenseRuleName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'licenseRuleName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('licenseStartDate')">
              <span v-text="t$('partnerTrackingFeesApp.license.licenseStartDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'licenseStartDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('licenseEndDate')">
              <span v-text="t$('partnerTrackingFeesApp.license.licenseEndDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'licenseEndDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('licenseQuantity')">
              <span v-text="t$('partnerTrackingFeesApp.license.licenseQuantity')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'licenseQuantity'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('pricePerLicense')">
              <span v-text="t$('partnerTrackingFeesApp.license.pricePerLicense')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'pricePerLicense'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('totalLicenseAmount')">
              <span v-text="t$('partnerTrackingFeesApp.license.totalLicenseAmount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalLicenseAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('client.id')">
              <span v-text="t$('partnerTrackingFeesApp.license.client')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'client.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('partner.id')">
              <span v-text="t$('partnerTrackingFeesApp.license.partner')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'partner.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('commissionRuleSet.id')">
              <span v-text="t$('partnerTrackingFeesApp.license.commissionRuleSet')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'commissionRuleSet.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="license in licenses" :key="license.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'LicenseView', params: { licenseId: license.id } }">{{ license.id }}</router-link>
            </td>
            <td>{{ license.licenseRuleName }}</td>
            <td>{{ license.licenseStartDate }}</td>
            <td>{{ license.licenseEndDate }}</td>
            <td>{{ license.licenseQuantity }}</td>
            <td>{{ license.pricePerLicense }}</td>
            <td>{{ license.totalLicenseAmount }}</td>
            <td>
              <div v-if="license.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: license.client.id } }">{{ license.client.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="license.partner">
                <router-link :to="{ name: 'PartnerView', params: { partnerId: license.partner.id } }">{{ license.partner.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="license.commissionRuleSet">
                <router-link :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: license.commissionRuleSet.id } }">{{
                  license.commissionRuleSet.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'LicenseView', params: { licenseId: license.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'LicenseEdit', params: { licenseId: license.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(license)"
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
          id="partnerTrackingFeesApp.license.delete.question"
          data-cy="licenseDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-license-heading" v-text="t$('partnerTrackingFeesApp.license.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-license"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeLicense()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="licenses && licenses.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./license.component.ts"></script>
