import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InvoiceService from './invoice.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import { type IClient } from '@/shared/model/client.model';
import PartnerService from '@/entities/partner/partner.service';
import { type IPartner } from '@/shared/model/partner.model';
import { type IInvoice, Invoice } from '@/shared/model/invoice.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InvoiceUpdate',
  setup() {
    const invoiceService = inject('invoiceService', () => new InvoiceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const invoice: Ref<IInvoice> = ref(new Invoice());

    const clientService = inject('clientService', () => new ClientService());

    const clients: Ref<IClient[]> = ref([]);

    const partnerService = inject('partnerService', () => new PartnerService());

    const partners: Ref<IPartner[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveInvoice = async invoiceId => {
      try {
        const res = await invoiceService().find(invoiceId);
        invoice.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.invoiceId) {
      retrieveInvoice(route.params.invoiceId);
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
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      invoiceAmount: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      invoiceDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      invoiceType: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      lineItems: {},
      client: {},
      partner: {},
    };
    const v$ = useVuelidate(validationRules, invoice as any);
    v$.value.$validate();

    return {
      invoiceService,
      alertService,
      invoice,
      previousState,
      isSaving,
      currentLanguage,
      clients,
      partners,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.invoice.id) {
        this.invoiceService()
          .update(this.invoice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.invoice.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.invoiceService()
          .create(this.invoice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.invoice.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
