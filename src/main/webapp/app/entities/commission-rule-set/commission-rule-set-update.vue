<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.commissionRuleSet.home.createOrEditLabel"
          data-cy="CommissionRuleSetCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.commissionRuleSet.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="commissionRuleSet.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="commissionRuleSet.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRuleSet.commissionRuleSetId')"
              for="commission-rule-set-commissionRuleSetId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="commissionRuleSetId"
              id="commission-rule-set-commissionRuleSetId"
              data-cy="commissionRuleSetId"
              :class="{ valid: !v$.commissionRuleSetId.$invalid, invalid: v$.commissionRuleSetId.$invalid }"
              v-model="v$.commissionRuleSetId.$model"
              required
            />
            <div v-if="v$.commissionRuleSetId.$anyDirty && v$.commissionRuleSetId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.commissionRuleSetId.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRuleSet.ruleSetName')"
              for="commission-rule-set-ruleSetName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ruleSetName"
              id="commission-rule-set-ruleSetName"
              data-cy="ruleSetName"
              :class="{ valid: !v$.ruleSetName.$invalid, invalid: v$.ruleSetName.$invalid }"
              v-model="v$.ruleSetName.$model"
              required
            />
            <div v-if="v$.ruleSetName.$anyDirty && v$.ruleSetName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ruleSetName.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./commission-rule-set-update.component.ts"></script>
