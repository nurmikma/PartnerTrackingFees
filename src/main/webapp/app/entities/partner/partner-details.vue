<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="partner">
        <h2 class="jh-entity-heading" data-cy="partnerDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.partner.detail.title')"></span> {{ partner.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.partner.partnerID')"></span>
          </dt>
          <dd>
            <span>{{ partner.partnerID }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.partner.partnerName')"></span>
          </dt>
          <dd>
            <span>{{ partner.partnerName }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="partner.id" :to="{ name: 'PartnerEdit', params: { partnerId: partner.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./partner-details.component.ts"></script>
