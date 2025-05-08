import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CommissionRuleSetService from './commission-rule-set.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { CommissionRuleSet, type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionRuleSetUpdate',
  setup() {
    const commissionRuleSetService = inject('commissionRuleSetService', () => new CommissionRuleSetService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const commissionRuleSet: Ref<ICommissionRuleSet> = ref(new CommissionRuleSet());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCommissionRuleSet = async commissionRuleSetId => {
      try {
        const res = await commissionRuleSetService().find(commissionRuleSetId);
        commissionRuleSet.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.commissionRuleSetId) {
      retrieveCommissionRuleSet(route.params.commissionRuleSetId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      commissionRuleSetId: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      ruleSetName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      rules: {},
    };
    const v$ = useVuelidate(validationRules, commissionRuleSet as any);
    v$.value.$validate();

    return {
      commissionRuleSetService,
      alertService,
      commissionRuleSet,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.commissionRuleSet.id) {
        this.commissionRuleSetService()
          .update(this.commissionRuleSet)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.commissionRuleSet.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.commissionRuleSetService()
          .create(this.commissionRuleSet)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.commissionRuleSet.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
