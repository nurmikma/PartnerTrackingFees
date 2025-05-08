<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.commissionFee.home.createOrEditLabel"
          data-cy="CommissionFeeCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.commissionFee.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="commissionFee.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="commissionFee.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionFee.commissionAmount')"
              for="commission-fee-commissionAmount"
            ></label>
            <input
              type="number"
              class="form-control"
              name="commissionAmount"
              id="commission-fee-commissionAmount"
              data-cy="commissionAmount"
              :class="{ valid: !v$.commissionAmount.$invalid, invalid: v$.commissionAmount.$invalid }"
              v-model.number="v$.commissionAmount.$model"
              required
            />
            <div v-if="v$.commissionAmount.$anyDirty && v$.commissionAmount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.commissionAmount.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionFee.license')"
              for="commission-fee-license"
            ></label>
            <select class="form-control" id="commission-fee-license" data-cy="license" name="license" v-model="commissionFee.license">
              <option :value="null"></option>
              <option
                :value="commissionFee.license && licenseOption.id === commissionFee.license.id ? commissionFee.license : licenseOption"
                v-for="licenseOption in licenses"
                :key="licenseOption.id"
              >
                {{ licenseOption.id }}
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
<script lang="ts" src="./commission-fee-update.component.ts"></script>
