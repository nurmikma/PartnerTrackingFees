<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.license.home.createOrEditLabel"
          data-cy="LicenseCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.license.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="license.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="license.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.licenseRuleName')"
              for="license-licenseRuleName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="licenseRuleName"
              id="license-licenseRuleName"
              data-cy="licenseRuleName"
              :class="{ valid: !v$.licenseRuleName.$invalid, invalid: v$.licenseRuleName.$invalid }"
              v-model="v$.licenseRuleName.$model"
              required
            />
            <div v-if="v$.licenseRuleName.$anyDirty && v$.licenseRuleName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.licenseRuleName.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.licenseStartDate')"
              for="license-licenseStartDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="license-licenseStartDate"
                  v-model="v$.licenseStartDate.$model"
                  name="licenseStartDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="license-licenseStartDate"
                data-cy="licenseStartDate"
                type="text"
                class="form-control"
                name="licenseStartDate"
                :class="{ valid: !v$.licenseStartDate.$invalid, invalid: v$.licenseStartDate.$invalid }"
                v-model="v$.licenseStartDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.licenseStartDate.$anyDirty && v$.licenseStartDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.licenseStartDate.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.licenseEndDate')"
              for="license-licenseEndDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="license-licenseEndDate"
                  v-model="v$.licenseEndDate.$model"
                  name="licenseEndDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="license-licenseEndDate"
                data-cy="licenseEndDate"
                type="text"
                class="form-control"
                name="licenseEndDate"
                :class="{ valid: !v$.licenseEndDate.$invalid, invalid: v$.licenseEndDate.$invalid }"
                v-model="v$.licenseEndDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.licenseQuantity')"
              for="license-licenseQuantity"
            ></label>
            <input
              type="number"
              class="form-control"
              name="licenseQuantity"
              id="license-licenseQuantity"
              data-cy="licenseQuantity"
              :class="{ valid: !v$.licenseQuantity.$invalid, invalid: v$.licenseQuantity.$invalid }"
              v-model.number="v$.licenseQuantity.$model"
              required
            />
            <div v-if="v$.licenseQuantity.$anyDirty && v$.licenseQuantity.$invalid">
              <small class="form-text text-danger" v-for="error of v$.licenseQuantity.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.pricePerLicense')"
              for="license-pricePerLicense"
            ></label>
            <input
              type="number"
              class="form-control"
              name="pricePerLicense"
              id="license-pricePerLicense"
              data-cy="pricePerLicense"
              :class="{ valid: !v$.pricePerLicense.$invalid, invalid: v$.pricePerLicense.$invalid }"
              v-model.number="v$.pricePerLicense.$model"
              required
            />
            <div v-if="v$.pricePerLicense.$anyDirty && v$.pricePerLicense.$invalid">
              <small class="form-text text-danger" v-for="error of v$.pricePerLicense.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.totalLicenseAmount')"
              for="license-totalLicenseAmount"
            ></label>
            <input
              type="number"
              class="form-control"
              name="totalLicenseAmount"
              id="license-totalLicenseAmount"
              data-cy="totalLicenseAmount"
              :class="{ valid: !v$.totalLicenseAmount.$invalid, invalid: v$.totalLicenseAmount.$invalid }"
              v-model.number="v$.totalLicenseAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.license.client')" for="license-client"></label>
            <select class="form-control" id="license-client" data-cy="client" name="client" v-model="license.client">
              <option :value="null"></option>
              <option
                :value="license.client && clientOption.id === license.client.id ? license.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.license.partner')" for="license-partner"></label>
            <select class="form-control" id="license-partner" data-cy="partner" name="partner" v-model="license.partner">
              <option :value="null"></option>
              <option
                :value="license.partner && partnerOption.id === license.partner.id ? license.partner : partnerOption"
                v-for="partnerOption in partners"
                :key="partnerOption.id"
              >
                {{ partnerOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.license.commissionRuleSet')"
              for="license-commissionRuleSet"
            ></label>
            <select
              class="form-control"
              id="license-commissionRuleSet"
              data-cy="commissionRuleSet"
              name="commissionRuleSet"
              v-model="license.commissionRuleSet"
            >
              <option :value="null"></option>
              <option
                :value="
                  license.commissionRuleSet && commissionRuleSetOption.id === license.commissionRuleSet.id
                    ? license.commissionRuleSet
                    : commissionRuleSetOption
                "
                v-for="commissionRuleSetOption in commissionRuleSets"
                :key="commissionRuleSetOption.id"
              >
                {{ commissionRuleSetOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./license-update.component.ts"></script>
