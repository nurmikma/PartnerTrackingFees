<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="license">
        <h2 class="jh-entity-heading" data-cy="licenseDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.license.detail.title')"></span> {{ license.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.licenseRuleName')"></span>
          </dt>
          <dd>
            <span>{{ license.licenseRuleName }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.licenseStartDate')"></span>
          </dt>
          <dd>
            <span>{{ license.licenseStartDate }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.licenseEndDate')"></span>
          </dt>
          <dd>
            <span>{{ license.licenseEndDate }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.licenseQuantity')"></span>
          </dt>
          <dd>
            <span>{{ license.licenseQuantity }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.pricePerLicense')"></span>
          </dt>
          <dd>
            <span>{{ license.pricePerLicense }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.totalLicenseAmount')"></span>
          </dt>
          <dd>
            <span>{{ license.totalLicenseAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.client')"></span>
          </dt>
          <dd>
            <div v-if="license.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: license.client.id } }">{{ license.client.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.partner')"></span>
          </dt>
          <dd>
            <div v-if="license.partner">
              <router-link :to="{ name: 'PartnerView', params: { partnerId: license.partner.id } }">{{ license.partner.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.license.commissionRuleSet')"></span>
          </dt>
          <dd>
            <div v-if="license.commissionRuleSet">
              <router-link :to="{ name: 'CommissionRuleSetView', params: { commissionRuleSetId: license.commissionRuleSet.id } }">{{
                license.commissionRuleSet.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="license.id" :to="{ name: 'LicenseEdit', params: { licenseId: license.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./license-details.component.ts"></script>
