<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="client">
        <h2 class="jh-entity-heading" data-cy="clientDetailsHeading">
          <span v-text="t$('partnerTrackingFeesApp.client.detail.title')"></span> {{ client.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.client.clientName')"></span>
          </dt>
          <dd>
            <span>{{ client.clientName }}</span>
          </dd>
          <dt>
            <span v-text="t$('partnerTrackingFeesApp.client.partner')"></span>
          </dt>
          <dd>
            <div v-if="client.partner">
              <router-link :to="{ name: 'PartnerView', params: { partnerId: client.partner.id } }">{{ client.partner.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="client.id" :to="{ name: 'ClientEdit', params: { clientId: client.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./client-details.component.ts"></script>
