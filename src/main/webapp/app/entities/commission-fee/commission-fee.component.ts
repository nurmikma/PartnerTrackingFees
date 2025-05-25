import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

import CommissionFeeService from './commission-fee.service';
import { type ICommissionFee } from '@/shared/model/commission-fee.model';
import { useAlertService } from '@/shared/alert/alert.service';
import axios from 'axios';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CommissionFee',
  setup() {
    const { t: t$ } = useI18n();
    const commissionFeeService = inject('commissionFeeService', () => new CommissionFeeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const commissionFees: Ref<ICommissionFee[]> = ref([]);

    const isFetching = ref(false);

    const selectedInvoiceId = ref<number | null>(null);
    const invoiceOptions = ref<Array<{ id: number; name: string }>>([]);
    const isFetchingInvoices = ref(false);

    const clear = () => {
      page.value = 1;
    };

    const loadInvoices = async () => {
      isFetchingInvoices.value = true;
      try {
        const res = await axios.get('/api/invoices');
        invoiceOptions.value = res.data.map((invoice: any) => ({
          id: invoice.id,
          name: `Invoice #${invoice.id}`,
        }));
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        isFetchingInvoices.value = false;
      }
    };

    const generateCommissionList = async () => {
      if (!selectedInvoiceId.value) return;

      try {
        // Call backend to generate commission fees for selected invoice
        await axios.post(`/api/commission-fees/generate`, { invoiceId: selectedInvoiceId.value });
        // Refresh list after generation
        await retrieveCommissionFees();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveCommissionFees = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
          invoiceId: selectedInvoiceId.value || undefined,
        };
        const res = await commissionFeeService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        commissionFees.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCommissionFees();
    };

    onMounted(async () => {
      await Promise.all([retrieveCommissionFees(), loadInvoices()]);
    });

    watch(selectedInvoiceId, async () => {
      clear();
      await retrieveCommissionFees();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICommissionFee) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCommissionFee = async () => {
      try {
        await commissionFeeService().delete(removeId.value);
        const message = t$('partnerTrackingFeesApp.commissionFee.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCommissionFees();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    watch([propOrder, reverse], async () => {
      if (page.value === 1) {
        await retrieveCommissionFees();
      } else {
        clear();
      }
    });

    watch(page, async () => {
      await retrieveCommissionFees();
    });

    return {
      commissionFees,
      handleSyncList,
      isFetching,
      retrieveCommissionFees,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCommissionFee,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      selectedInvoiceId,
      invoiceOptions,
      isFetchingInvoices,
      generateCommissionList,
      t$,
    };
  },
});
