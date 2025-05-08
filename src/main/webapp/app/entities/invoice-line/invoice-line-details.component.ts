import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import InvoiceLineService from './invoice-line.service';
import { type IInvoiceLine } from '@/shared/model/invoice-line.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InvoiceLineDetails',
  setup() {
    const invoiceLineService = inject('invoiceLineService', () => new InvoiceLineService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const invoiceLine: Ref<IInvoiceLine> = ref({});

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

    return {
      alertService,
      invoiceLine,

      previousState,
      t$: useI18n().t,
    };
  },
});
