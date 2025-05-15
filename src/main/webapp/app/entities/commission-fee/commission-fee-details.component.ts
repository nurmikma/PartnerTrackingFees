import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CommissionFeeService from './commission-fee.service';
import { type ICommissionFee } from '@/shared/model/commission-fee.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionFeeDetails',
  setup() {
    const commissionFeeService = inject('commissionFeeService', () => new CommissionFeeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const commissionFee: Ref<ICommissionFee> = ref({});

    const retrieveCommissionFee = async commissionFeeId => {
      try {
        const res = await commissionFeeService().find(commissionFeeId);
        commissionFee.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.commissionFeeId) {
      retrieveCommissionFee(route.params.commissionFeeId);
    }

    return {
      alertService,
      commissionFee,

      previousState,
      t$: useI18n().t,
    };
  },
});
