import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PartnerService from './partner.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPartner, Partner } from '@/shared/model/partner.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PartnerUpdate',
  setup() {
    const partnerService = inject('partnerService', () => new PartnerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const partner: Ref<IPartner> = ref(new Partner());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      partnerID: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      partnerName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, partner as any);
    v$.value.$validate();

    return {
      partnerService,
      alertService,
      partner,
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
      if (this.partner.id) {
        this.partnerService()
          .update(this.partner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.partner.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.partnerService()
          .create(this.partner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.partner.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
