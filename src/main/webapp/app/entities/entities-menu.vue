<template>
  <div>
    <b-dropdown-item to="/partner">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.partner')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/client">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.client')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/license">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.license')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/invoice">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.invoice')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/invoice-line">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.invoiceLine')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/commission-rule">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.commissionRule')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/commission-rule-set">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.commissionRuleSet')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/commission-fee">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.commissionFee')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
