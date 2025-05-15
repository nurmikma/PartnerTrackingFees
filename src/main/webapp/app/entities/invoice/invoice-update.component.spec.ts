import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InvoiceUpdate from './invoice-update.vue';
import InvoiceService from './invoice.service';
import AlertService from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import PartnerService from '@/entities/partner/partner.service';

type InvoiceUpdateComponentType = InstanceType<typeof InvoiceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const invoiceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InvoiceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Invoice Management Update Component', () => {
    let comp: InvoiceUpdateComponentType;
    let invoiceServiceStub: SinonStubbedInstance<InvoiceService>;

    beforeEach(() => {
      route = {};
      invoiceServiceStub = sinon.createStubInstance<InvoiceService>(InvoiceService);
      invoiceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          invoiceService: () => invoiceServiceStub,
          clientService: () =>
            sinon.createStubInstance<ClientService>(ClientService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          partnerService: () =>
            sinon.createStubInstance<PartnerService>(PartnerService, {
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
        const wrapper = shallowMount(InvoiceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.invoice = invoiceSample;
        invoiceServiceStub.update.resolves(invoiceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceServiceStub.update.calledWith(invoiceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        invoiceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InvoiceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.invoice = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        invoiceServiceStub.find.resolves(invoiceSample);
        invoiceServiceStub.retrieve.resolves([invoiceSample]);

        // WHEN
        route = {
          params: {
            invoiceId: `${invoiceSample.id}`,
          },
        };
        const wrapper = shallowMount(InvoiceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.invoice).toMatchObject(invoiceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        invoiceServiceStub.find.resolves(invoiceSample);
        const wrapper = shallowMount(InvoiceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
