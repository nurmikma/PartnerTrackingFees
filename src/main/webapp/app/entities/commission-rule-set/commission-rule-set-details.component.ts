import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CommissionRuleSetService from './commission-rule-set.service';
import { type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionRuleSetDetails',
  setup() {
    const commissionRuleSetService = inject('commissionRuleSetService', () => new CommissionRuleSetService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const commissionRuleSet: Ref<ICommissionRuleSet> = ref({});

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

    return {
      alertService,
      commissionRuleSet,

      previousState,
      t$: useI18n().t,
    };
  },
});
