<template>
  <div>
    <h2 id="page-heading" data-cy="CommissionRuleHeading">
      <span v-text="t$('partnerTrackingFeesApp.commissionRule.home.title')" id="commission-rule-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('partnerTrackingFeesApp.commissionRule.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CommissionRuleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-commission-rule"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && commissionRules && commissionRules.length === 0">
      <span v-text="t$('partnerTrackingFeesApp.commissionRule.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="commissionRules && commissionRules.length > 0">
      <table class="table table-striped" aria-describedby="commissionRules">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ruleName')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.ruleName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ruleName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('description')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('startDay')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.startDay')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDay'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('endDay')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.endDay')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDay'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('commissionPercentage')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.commissionPercentage')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'commissionPercentage'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('commissionRuleSet.id')">
              <span v-text="t$('partnerTrackingFeesApp.commissionRule.commissionRuleSet')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'commissionRuleSet.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="commissionRule in commissionRules" :key="commissionRule.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CommissionRuleView', params: { commissionRuleId: commissionRule.id } }">{{
                commissionRule.id
              }}</router-link>
            </td>
            <td>{{ commissionRule.ruleName }}</td>
            <td>{{ commissionRule.description }}</td>
            <td>{{ commissionRule.startDay }}</td>
            <td>{{ commissionRule.endDay }}</td>
            <td>{{ commissionRule.commissionPercentage }}</td>
            <td>
              <div v-if="commissionRule.commissionRuleSet">
                <router-link
                  :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: commissionRule.commissionRuleSet.id } }"
                  >{{ commissionRule.commissionRuleSet.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CommissionRuleView', params: { commissionRuleId: commissionRule.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'CommissionRuleEdit', params: { commissionRuleId: commissionRule.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(commissionRule)"
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
          id="partnerTrackingFeesApp.commissionRule.delete.question"
          data-cy="commissionRuleDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-commissionRule-heading"
          v-text="t$('partnerTrackingFeesApp.commissionRule.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-commissionRule"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCommissionRule()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="commissionRules && commissionRules.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./commission-rule.component.ts"></script>
