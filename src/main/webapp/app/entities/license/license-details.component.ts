import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import LicenseService from './license.service';
import { type ILicense } from '@/shared/model/license.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LicenseDetails',
  setup() {
    const licenseService = inject('licenseService', () => new LicenseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const license: Ref<ILicense> = ref({});

    const retrieveLicense = async licenseId => {
      try {
        const res = await licenseService().find(licenseId);
        license.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.licenseId) {
      retrieveLicense(route.params.licenseId);
    }

    return {
      alertService,
      license,

      previousState,
      t$: useI18n().t,
    };
  },
});
