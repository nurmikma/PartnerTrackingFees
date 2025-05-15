import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CommissionFeeService from './commission-fee.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LicenseService from '@/entities/license/license.service';
import { type ILicense } from '@/shared/model/license.model';
import { CommissionFee, type ICommissionFee } from '@/shared/model/commission-fee.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionFeeUpdate',
  setup() {
    const commissionFeeService = inject('commissionFeeService', () => new CommissionFeeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const commissionFee: Ref<ICommissionFee> = ref(new CommissionFee());

    const licenseService = inject('licenseService', () => new LicenseService());

    const licenses: Ref<ILicense[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      licenseService()
        .retrieve()
        .then(res => {
          licenses.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      commissionAmount: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      license: {},
    };
    const v$ = useVuelidate(validationRules, commissionFee as any);
    v$.value.$validate();

    return {
      commissionFeeService,
      alertService,
      commissionFee,
      previousState,
      isSaving,
      currentLanguage,
      licenses,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.commissionFee.id) {
        this.commissionFeeService()
          .update(this.commissionFee)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.commissionFee.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.commissionFeeService()
          .create(this.commissionFee)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.commissionFee.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
