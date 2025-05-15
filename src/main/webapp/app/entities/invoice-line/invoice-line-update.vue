<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.invoiceLine.home.createOrEditLabel"
          data-cy="InvoiceLineCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.invoiceLine.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="invoiceLine.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="invoiceLine.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.invoiceLine.totalAmount')"
              for="invoice-line-totalAmount"
            ></label>
            <input
              type="number"
              class="form-control"
              name="totalAmount"
              id="invoice-line-totalAmount"
              data-cy="totalAmount"
              :class="{ valid: !v$.totalAmount.$invalid, invalid: v$.totalAmount.$invalid }"
              v-model.number="v$.totalAmount.$model"
              required
            />
            <div v-if="v$.totalAmount.$anyDirty && v$.totalAmount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.totalAmount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoiceLine.license')" for="invoice-line-license"></label>
            <select class="form-control" id="invoice-line-license" data-cy="license" name="license" v-model="invoiceLine.license">
              <option :value="null"></option>
              <option
                :value="invoiceLine.license && licenseOption.id === invoiceLine.license.id ? invoiceLine.license : licenseOption"
                v-for="licenseOption in licenses"
                :key="licenseOption.id"
              >
                {{ licenseOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.invoiceLine.invoice')" for="invoice-line-invoice"></label>
            <select class="form-control" id="invoice-line-invoice" data-cy="invoice" name="invoice" v-model="invoiceLine.invoice">
              <option :value="null"></option>
              <option
                :value="invoiceLine.invoice && invoiceOption.id === invoiceLine.invoice.id ? invoiceLine.invoice : invoiceOption"
                v-for="invoiceOption in invoices"
                :key="invoiceOption.id"
              >
                {{ invoiceOption.id }}
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
<script lang="ts" src="./invoice-line-update.component.ts"></script>
