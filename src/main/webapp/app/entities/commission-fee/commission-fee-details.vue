<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commissionFee">
        <h2 class="jh-entity-heading" data-cy="commissionFeeDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.commissionFee.detail.title')"></span> {{ commissionFee.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionFee.commissionAmount')"></span>
          </dt>
          <dd>
            <span>{{ commissionFee.commissionAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.commissionFee.license')"></span>
          </dt>
          <dd>
            <div v-if="commissionFee.license">
              <router-link :to="{ name: 'LicenseView', params: { licenseId: commissionFee.license.id } }">{{
                commissionFee.license.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="commissionFee.id"
          :to="{ name: 'CommissionFeeEdit', params: { commissionFeeId: commissionFee.id } }"
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

<script lang="ts" src="./commission-fee-details.component.ts"></script>
