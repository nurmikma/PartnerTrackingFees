import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import InvoiceLine from './invoice-line.vue';
import InvoiceLineService from './invoice-line.service';
import AlertService from '@/shared/alert/alert.service';

type InvoiceLineComponentType = InstanceType<typeof InvoiceLine>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('InvoiceLine Management Component', () => {
    let invoiceLineServiceStub: SinonStubbedInstance<InvoiceLineService>;
    let mountOptions: MountingOptions<InvoiceLineComponentType>['global'];

    beforeEach(() => {
      invoiceLineServiceStub = sinon.createStubInstance<InvoiceLineService>(InvoiceLineService);
      invoiceLineServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          invoiceLineService: () => invoiceLineServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        invoiceLineServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(InvoiceLine, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.invoiceLines[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(InvoiceLine, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: InvoiceLineComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(InvoiceLine, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        invoiceLineServiceStub.retrieve.reset();
        invoiceLineServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        invoiceLineServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.retrieve.called).toBeTruthy();
        expect(comp.invoiceLines[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(invoiceLineServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        invoiceLineServiceStub.retrieve.reset();
        invoiceLineServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(invoiceLineServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.invoiceLines[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(invoiceLineServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        invoiceLineServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeInvoiceLine();
        await comp.$nextTick(); // clear components

        // THEN
        expect(invoiceLineServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(invoiceLineServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
