import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InvoiceLineDetails from './invoice-line-details.vue';
import InvoiceLineService from './invoice-line.service';
import AlertService from '@/shared/alert/alert.service';

type InvoiceLineDetailsComponentType = InstanceType<typeof InvoiceLineDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const invoiceLineSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('InvoiceLine Management Detail Component', () => {
    let invoiceLineServiceStub: SinonStubbedInstance<InvoiceLineService>;
    let mountOptions: MountingOptions<InvoiceLineDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      invoiceLineServiceStub = sinon.createStubInstance<InvoiceLineService>(InvoiceLineService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          invoiceLineService: () => invoiceLineServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        invoiceLineServiceStub.find.resolves(invoiceLineSample);
        route = {
          params: {
            invoiceLineId: `${123}`,
          },
        };
        const wrapper = shallowMount(InvoiceLineDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.invoiceLine).toMatchObject(invoiceLineSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        invoiceLineServiceStub.find.resolves(invoiceLineSample);
        const wrapper = shallowMount(InvoiceLineDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
