import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PartnerService from './partner.service';
import { type IPartner } from '@/shared/model/partner.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PartnerDetails',
  setup() {
    const partnerService = inject('partnerService', () => new PartnerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const partner: Ref<IPartner> = ref({});

    const retrievePartner = async partnerId => {
      try {
        const res = await partnerService().find(partnerId);
        partner.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.partnerId) {
      retrievePartner(route.params.partnerId);
    }

    return {
      alertService,
      partner,

      previousState,
      t$: useI18n().t,
    };
  },
});
