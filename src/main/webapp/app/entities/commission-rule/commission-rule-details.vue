<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commissionRule">
        <h2 class="jh-entity-heading" data-cy="commissionRuleDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.commissionRule.detail.title')"></span> {{ commissionRule.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.ruleName')"></span>
          </dt>
          <dd>
            <span>{{ commissionRule.ruleName }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.description')"></span>
          </dt>
          <dd>
            <span>{{ commissionRule.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.startDay')"></span>
          </dt>
          <dd>
            <span>{{ commissionRule.startDay }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.endDay')"></span>
          </dt>
          <dd>
            <span>{{ commissionRule.endDay }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.commissionPercentage')"></span>
          </dt>
          <dd>
            <span>{{ commissionRule.commissionPercentage }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionRule.commissionRuleSet')"></span>
          </dt>
          <dd>
            <div v-if="commissionRule.commissionRuleSet">
              <router-link :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: commissionRule.commissionRuleSet.id } }">{{
                commissionRule.commissionRuleSet.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="commissionRule.id"
          :to="{ name: 'CommissionRuleEdit', params: { commissionRuleId: commissionRule.id } }"
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

<script lang="ts" src="./commission-rule-details.component.ts"></script>
