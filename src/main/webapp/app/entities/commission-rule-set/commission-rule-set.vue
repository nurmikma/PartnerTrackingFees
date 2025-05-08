<template>
  <div>
    <h2 id="page-heading" data-cy="CommissionRuleSetHeading">
      <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.home.title')" id="commission-rule-set-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CommissionRuleSetCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-commission-rule-set"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && commissionRuleSets && commissionRuleSets.length === 0">
      <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="commissionRuleSets && commissionRuleSets.length > 0">
      <table class="table table-striped" aria-describedby="commissionRuleSets">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('commissionRuleSetId')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.commissionRuleSetId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'commissionRuleSetId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ruleSetName')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRuleSet.ruleSetName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ruleSetName'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="commissionRuleSet in commissionRuleSets" :key="commissionRuleSet.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: commissionRuleSet.id } }">{{
                commissionRuleSet.id
              }}</router-link>
            </td>
            <td>{{ commissionRuleSet.commissionRuleSetId }}</td>
            <td>{{ commissionRuleSet.ruleSetName }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: commissionRuleSet.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'CommissionRuleSetEdit', params: { commissionRuleSetId: commissionRuleSet.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(commissionRuleSet)"
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
          id="partnerTrackingFeesApp.commissionRuleSet.delete.question"
          data-cy="commissionRuleSetDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-commissionRuleSet-heading"
          v-text="t$('partnerTrackingFeesApp.commissionRuleSet.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-commissionRuleSet"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCommissionRuleSet()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="commissionRuleSets && commissionRuleSets.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./commission-rule-set.component.ts"></script>
