<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.partner.home.createOrEditLabel"
          data-cy="PartnerCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.partner.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="partner.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="partner.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.partner.partnerID')" for="partner-partnerID"></label>
            <input
              type="text"
              class="form-control"
              name="partnerID"
              id="partner-partnerID"
              data-cy="partnerID"
              :class="{ valid: !v$.partnerID.$invalid, invalid: v$.partnerID.$invalid }"
              v-model="v$.partnerID.$model"
              required
            />
            <div v-if="v$.partnerID.$anyDirty && v$.partnerID.$invalid">
              <small class="form-text text-danger" v-for="error of v$.partnerID.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.partner.partnerName')" for="partner-partnerName"></label>
            <input
              type="text"
              class="form-control"
              name="partnerName"
              id="partner-partnerName"
              data-cy="partnerName"
              :class="{ valid: !v$.partnerName.$invalid, invalid: v$.partnerName.$invalid }"
              v-model="v$.partnerName.$model"
              required
            />
            <div v-if="v$.partnerName.$anyDirty && v$.partnerName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.partnerName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./partner-update.component.ts"></script>
