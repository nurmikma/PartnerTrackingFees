import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InvoiceLineService from './invoice-line.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LicenseService from '@/entities/license/license.service';
import { type ILicense } from '@/shared/model/license.model';
import InvoiceService from '@/entities/invoice/invoice.service';
import { type IInvoice } from '@/shared/model/invoice.model';
import { type IInvoiceLine, InvoiceLine } from '@/shared/model/invoice-line.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InvoiceLineUpdate',
  setup() {
    const invoiceLineService = inject('invoiceLineService', () => new InvoiceLineService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const invoiceLine: Ref<IInvoiceLine> = ref(new InvoiceLine());

    const licenseService = inject('licenseService', () => new LicenseService());

    const licenses: Ref<ILicense[]> = ref([]);

    const invoiceService = inject('invoiceService', () => new InvoiceService());

    const invoices: Ref<IInvoice[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveInvoiceLine = async invoiceLineId => {
      try {
        const res = await invoiceLineService().find(invoiceLineId);
        invoiceLine.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.invoiceLineId) {
      retrieveInvoiceLine(route.params.invoiceLineId);
    }

    const initRelationships = () => {
      licenseService()
        .retrieve()
        .then(res => {
          licenses.value = res.data;
        });
      invoiceService()
        .retrieve()
        .then(res => {
          invoices.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      totalAmount: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      license: {},
      invoice: {},
    };
    const v$ = useVuelidate(validationRules, invoiceLine as any);
    v$.value.$validate();

    return {
      invoiceLineService,
      alertService,
      invoiceLine,
      previousState,
      isSaving,
      currentLanguage,
      licenses,
      invoices,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.invoiceLine.id) {
        this.invoiceLineService()
          .update(this.invoiceLine)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('partnerTrackingFeesApp.invoiceLine.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.invoiceLineService()
          .create(this.invoiceLine)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('partnerTrackingFeesApp.invoiceLine.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
