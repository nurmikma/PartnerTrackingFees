import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CommissionFee from './commission-fee.vue';
import CommissionFeeService from './commission-fee.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionFeeComponentType = InstanceType<typeof CommissionFee>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CommissionFee Management Component', () => {
    let commissionFeeServiceStub: SinonStubbedInstance<CommissionFeeService>;
    let mountOptions: MountingOptions<CommissionFeeComponentType>['global'];

    beforeEach(() => {
      commissionFeeServiceStub = sinon.createStubInstance<CommissionFeeService>(CommissionFeeService);
      commissionFeeServiceStub.retrieve.resolves({ headers: {} });

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
          commissionFeeService: () => commissionFeeServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionFeeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CommissionFee, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.commissionFees[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CommissionFee, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CommissionFeeComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CommissionFee, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        commissionFeeServiceStub.retrieve.reset();
        commissionFeeServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        commissionFeeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.retrieve.called).toBeTruthy();
        expect(comp.commissionFees[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(commissionFeeServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        commissionFeeServiceStub.retrieve.reset();
        commissionFeeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(commissionFeeServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.commissionFees[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        commissionFeeServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCommissionFee();
        await comp.$nextTick(); // clear components

        // THEN
        expect(commissionFeeServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(commissionFeeServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
