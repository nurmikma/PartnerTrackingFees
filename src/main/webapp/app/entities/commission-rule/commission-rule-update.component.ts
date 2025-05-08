import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CommissionRuleService from './commission-rule.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CommissionRuleSetService from '@/entities/commission-rule-set/commission-rule-set.service';
import { type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';
import { CommissionRule, type ICommissionRule } from '@/shared/model/commission-rule.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionRuleUpdate',
  setup() {
    const commissionRuleService = inject('commissionRuleService', () => new CommissionRuleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const commissionRule: Ref<ICommissionRule> = ref(new CommissionRule());

    const commissionRuleSetService = inject('commissionRuleSetService', () => new CommissionRuleSetService());

    const commissionRuleSets: Ref<ICommissionRuleSet[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCommissionRule = async commissionRuleId => {
      try {
        const res = await commissionRuleService().find(commissionRuleId);
        commissionRule.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.commissionRuleId) {
      retrieveCommissionRule(route.params.commissionRuleId);
    }

    const initRelationships = () => {
      commissionRuleSetService()
        .retrieve()
        .then(res => {
          commissionRuleSets.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      ruleName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      description: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      startDay: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      endDay: {},
      commissionPercentage: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      commissionRuleSet: {},
    };
    const v$ = useVuelidate(validationRules, commissionRule as any);
    v$.value.$validate();

    return {
      commissionRuleService,
      alertService,
      commissionRule,
      previousState,
      isSaving,
      currentLanguage,
      commissionRuleSets,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.commissionRule.id) {
        this.commissionRuleService()
          .update(this.commissionRule)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.commissionRule.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.commissionRuleService()
          .create(this.commissionRule)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.commissionRule.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
