import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InvoiceLineUpdate from './invoice-line-update.vue';
import InvoiceLineService from './invoice-line.service';
import AlertService from '@/shared/alert/alert.service';

import LicenseService from '@/entities/license/license.service';
import InvoiceService from '@/entities/invoice/invoice.service';

type InvoiceLineUpdateComponentType = InstanceType<typeof InvoiceLineUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const invoiceLineSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InvoiceLineUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('InvoiceLine Management Update Component', () => {
    let comp: InvoiceLineUpdateComponentType;
    let invoiceLineServiceStub: SinonStubbedInstance<InvoiceLineService>;

    beforeEach(() => {
      route = {};
      invoiceLineServiceStub = sinon.createStubInstance<InvoiceLineService>(InvoiceLineService);
      invoiceLineServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          invoiceLineService: () => invoiceLineServiceStub,
          licenseService: () =>
            sinon.createStubInstance<LicenseService>(LicenseService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          invoiceService: () =>
            sinon.createStubInstance<InvoiceService>(InvoiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(InvoiceLineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.invoiceLine = invoiceLineSample;
        invoiceLineServiceStub.update.resolves(invoiceLineSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.update.calledWith(invoiceLineSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        invoiceLineServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InvoiceLineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.invoiceLine = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        invoiceLineServiceStub.find.resolves(invoiceLineSample);
        invoiceLineServiceStub.retrieve.resolves([invoiceLineSample]);

        // WHEN
        route = {
          params: {
            invoiceLineId: `${invoiceLineSample.id}`,
          },
        };
        const wrapper = shallowMount(InvoiceLineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.invoiceLine).toMatchObject(invoiceLineSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        invoiceLineServiceStub.find.resolves(invoiceLineSample);
        const wrapper = shallowMount(InvoiceLineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
