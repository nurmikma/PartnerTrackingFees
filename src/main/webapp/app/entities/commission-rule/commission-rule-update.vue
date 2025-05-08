<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="partnerTrackingFeesApp.commissionRule.home.createOrEditLabel"
          data-cy="CommissionRuleCreateUpdateHeading"
          v-text="t$('partnerTrackingFeesApp.commissionRule.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="commissionRule.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="commissionRule.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.ruleName')"
              for="commission-rule-ruleName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ruleName"
              id="commission-rule-ruleName"
              data-cy="ruleName"
              :class="{ valid: !v$.ruleName.$invalid, invalid: v$.ruleName.$invalid }"
              v-model="v$.ruleName.$model"
              required
            />
            <div v-if="v$.ruleName.$anyDirty && v$.ruleName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ruleName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.description')"
              for="commission-rule-description"
            ></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="commission-rule-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
              required
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.startDay')"
              for="commission-rule-startDay"
            ></label>
            <input
              type="number"
              class="form-control"
              name="startDay"
              id="commission-rule-startDay"
              data-cy="startDay"
              :class="{ valid: !v$.startDay.$invalid, invalid: v$.startDay.$invalid }"
              v-model.number="v$.startDay.$model"
              required
            />
            <div v-if="v$.startDay.$anyDirty && v$.startDay.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startDay.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.endDay')"
              for="commission-rule-endDay"
            ></label>
            <input
              type="number"
              class="form-control"
              name="endDay"
              id="commission-rule-endDay"
              data-cy="endDay"
              :class="{ valid: !v$.endDay.$invalid, invalid: v$.endDay.$invalid }"
              v-model.number="v$.endDay.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.commissionPercentage')"
              for="commission-rule-commissionPercentage"
            ></label>
            <input
              type="number"
              class="form-control"
              name="commissionPercentage"
              id="commission-rule-commissionPercentage"
              data-cy="commissionPercentage"
              :class="{ valid: !v$.commissionPercentage.$invalid, invalid: v$.commissionPercentage.$invalid }"
              v-model.number="v$.commissionPercentage.$model"
              required
            />
            <div v-if="v$.commissionPercentage.$anyDirty && v$.commissionPercentage.$invalid">
              <small class="form-text text-danger" v-for="error of v$.commissionPercentage.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('partnerTrackingFeesApp.commissionRule.commissionRuleSet')"
              for="commission-rule-commissionRuleSet"
            ></label>
            <select
              class="form-control"
              id="commission-rule-commissionRuleSet"
              data-cy="commissionRuleSet"
              name="commissionRuleSet"
              v-model="commissionRule.commissionRuleSet"
            >
              <option :value="null"></option>
              <option
                :value="
                  commissionRule.commissionRuleSet && commissionRuleSetOption.id === commissionRule.commissionRuleSet.id
                    ? commissionRule.commissionRuleSet
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
<script lang="ts" src="./commission-rule-update.component.ts"></script>
