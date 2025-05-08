import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CommissionRuleSet from './commission-rule-set.vue';
import CommissionRuleSetService from './commission-rule-set.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionRuleSetComponentType = InstanceType<typeof CommissionRuleSet>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CommissionRuleSet Management Component', () => {
    let commissionRuleSetServiceStub: SinonStubbedInstance<CommissionRuleSetService>;
    let mountOptions: MountingOptions<CommissionRuleSetComponentType>['global'];

    beforeEach(() => {
      commissionRuleSetServiceStub = sinon.createStubInstance<CommissionRuleSetService>(CommissionRuleSetService);
      commissionRuleSetServiceStub.retrieve.resolves({ headers: {} });

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
          commissionRuleSetService: () => commissionRuleSetServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionRuleSetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CommissionRuleSet, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.commissionRuleSets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CommissionRuleSet, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CommissionRuleSetComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CommissionRuleSet, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        commissionRuleSetServiceStub.retrieve.reset();
        commissionRuleSetServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        commissionRuleSetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.retrieve.called).toBeTruthy();
        expect(comp.commissionRuleSets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(commissionRuleSetServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        commissionRuleSetServiceStub.retrieve.reset();
        commissionRuleSetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(commissionRuleSetServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.commissionRuleSets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        commissionRuleSetServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCommissionRuleSet();
        await comp.$nextTick(); // clear components

        // THEN
        expect(commissionRuleSetServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(commissionRuleSetServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
