<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.invoice.home.createOrEditLabel"
          data-cy="InvoiceCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.invoice.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="invoice.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="invoice.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.invoiceId')" for="invoice-invoiceId"></label>
            <input
              type="text"
              class="form-control"
              name="invoiceId"
              id="invoice-invoiceId"
              data-cy="invoiceId"
              :class="{ valid: !v$.invoiceId.$invalid, invalid: v$.invoiceId.$invalid }"
              v-model="v$.invoiceId.$model"
              required
            />
            <div v-if="v$.invoiceId.$anyDirty && v$.invoiceId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.invoiceId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.clientId')" for="invoice-clientId"></label>
            <input
              type="text"
              class="form-control"
              name="clientId"
              id="invoice-clientId"
              data-cy="clientId"
              :class="{ valid: !v$.clientId.$invalid, invalid: v$.clientId.$invalid }"
              v-model="v$.clientId.$model"
              required
            />
            <div v-if="v$.clientId.$anyDirty && v$.clientId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.clientId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.partnerId')" for="invoice-partnerId"></label>
            <input
              type="text"
              class="form-control"
              name="partnerId"
              id="invoice-partnerId"
              data-cy="partnerId"
              :class="{ valid: !v$.partnerId.$invalid, invalid: v$.partnerId.$invalid }"
              v-model="v$.partnerId.$model"
              required
            />
            <div v-if="v$.partnerId.$anyDirty && v$.partnerId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.partnerId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.invoice.invoiceAmount')"
              for="invoice-invoiceAmount"
            ></label>
            <input
              type="number"
              class="form-control"
              name="invoiceAmount"
              id="invoice-invoiceAmount"
              data-cy="invoiceAmount"
              :class="{ valid: !v$.invoiceAmount.$invalid, invalid: v$.invoiceAmount.$invalid }"
              v-model.number="v$.invoiceAmount.$model"
              required
            />
            <div v-if="v$.invoiceAmount.$anyDirty && v$.invoiceAmount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.invoiceAmount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.invoiceDate')" for="invoice-invoiceDate"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="invoice-invoiceDate"
                  v-model="v$.invoiceDate.$model"
                  name="invoiceDate"
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
                id="invoice-invoiceDate"
                data-cy="invoiceDate"
                type="text"
                class="form-control"
                name="invoiceDate"
                :class="{ valid: !v$.invoiceDate.$invalid, invalid: v$.invoiceDate.$invalid }"
                v-model="v$.invoiceDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.invoiceDate.$anyDirty && v$.invoiceDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.invoiceDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.invoiceType')" for="invoice-invoiceType"></label>
            <input
              type="text"
              class="form-control"
              name="invoiceType"
              id="invoice-invoiceType"
              data-cy="invoiceType"
              :class="{ valid: !v$.invoiceType.$invalid, invalid: v$.invoiceType.$invalid }"
              v-model="v$.invoiceType.$model"
              required
            />
            <div v-if="v$.invoiceType.$anyDirty && v$.invoiceType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.invoiceType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.client')" for="invoice-client"></label>
            <select class="form-control" id="invoice-client" data-cy="client" name="client" v-model="invoice.client">
              <option :value="null"></option>
              <option
                :value="invoice.client && clientOption.id === invoice.client.id ? invoice.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoice.partner')" for="invoice-partner"></label>
            <select class="form-control" id="invoice-partner" data-cy="partner" name="partner" v-model="invoice.partner">
              <option :value="null"></option>
              <option
                :value="invoice.partner && partnerOption.id === invoice.partner.id ? invoice.partner : partnerOption"
                v-for="partnerOption in partners"
                :key="partnerOption.id"
              >
                {{ partnerOption.id }}
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
<script lang="ts" src="./invoice-update.component.ts"></script>
