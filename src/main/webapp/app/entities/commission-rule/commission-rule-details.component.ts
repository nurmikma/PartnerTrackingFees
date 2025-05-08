import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CommissionRuleService from './commission-rule.service';
import { type ICommissionRule } from '@/shared/model/commission-rule.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionRuleDetails',
  setup() {
    const commissionRuleService = inject('commissionRuleService', () => new CommissionRuleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const commissionRule: Ref<ICommissionRule> = ref({});

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

    return {
      alertService,
      commissionRule,

      previousState,
      t$: useI18n().t,
    };
  },
});
