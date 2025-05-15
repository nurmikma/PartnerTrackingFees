<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.client.home.createOrEditLabel"
          data-cy="ClientCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.client.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="client.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="client.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.client.clientName')" for="client-clientName"></label>
            <input
              type="text"
              class="form-control"
              name="clientName"
              id="client-clientName"
              data-cy="clientName"
              :class="{ valid: !v$.clientName.$invalid, invalid: v$.clientName.$invalid }"
              v-model="v$.clientName.$model"
              required
            />
            <div v-if="v$.clientName.$anyDirty && v$.clientName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.clientName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('partnerTrackingFeesApp.client.partner')" for="client-partner"></label>
            <select class="form-control" id="client-partner" data-cy="partner" name="partner" v-model="client.partner">
              <option :value="null"></option>
              <option
                :value="client.partner && partnerOption.id === client.partner.id ? client.partner : partnerOption"
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
<script lang="ts" src="./client-update.component.ts"></script>
