import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import LicenseService from './license.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import { type IClient } from '@/shared/model/client.model';
import PartnerService from '@/entities/partner/partner.service';
import { type IPartner } from '@/shared/model/partner.model';
import CommissionRuleSetService from '@/entities/commission-rule-set/commission-rule-set.service';
import { type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';
import { type ILicense, License } from '@/shared/model/license.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LicenseUpdate',
  setup() {
    const licenseService = inject('licenseService', () => new LicenseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const license: Ref<ILicense> = ref(new License());

    const clientService = inject('clientService', () => new ClientService());

    const clients: Ref<IClient[]> = ref([]);

    const partnerService = inject('partnerService', () => new PartnerService());

    const partners: Ref<IPartner[]> = ref([]);

    const commissionRuleSetService = inject('commissionRuleSetService', () => new CommissionRuleSetService());

    const commissionRuleSets: Ref<ICommissionRuleSet[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      clientService()
        .retrieve()
        .then(res => {
          clients.value = res.data;
        });
      partnerService()
        .retrieve()
        .then(res => {
          partners.value = res.data;
        });
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
      licenseRuleName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      licenseStartDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      licenseEndDate: {},
      licenseQuantity: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      pricePerLicense: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      totalLicenseAmount: {},
      client: {},
      partner: {},
      commissionRuleSet: {},
      invoiceLine: {},
      commissionFee: {},
    };
    const v$ = useVuelidate(validationRules, license as any);
    v$.value.$validate();

    return {
      licenseService,
      alertService,
      license,
      previousState,
      isSaving,
      currentLanguage,
      clients,
      partners,
      commissionRuleSets,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.license.id) {
        this.licenseService()
          .update(this.license)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.license.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.licenseService()
          .create(this.license)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.license.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
