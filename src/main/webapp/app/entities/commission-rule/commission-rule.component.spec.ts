import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CommissionRule from './commission-rule.vue';
import CommissionRuleService from './commission-rule.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionRuleComponentType = InstanceType<typeof CommissionRule>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CommissionRule Management Component', () => {
    let commissionRuleServiceStub: SinonStubbedInstance<CommissionRuleService>;
    let mountOptions: MountingOptions<CommissionRuleComponentType>['global'];

    beforeEach(() => {
      commissionRuleServiceStub = sinon.createStubInstance<CommissionRuleService>(CommissionRuleService);
      commissionRuleServiceStub.retrieve.resolves({ headers: {} });

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
          commissionRuleService: () => commissionRuleServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionRuleServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CommissionRule, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.commissionRules[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CommissionRule, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CommissionRuleComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CommissionRule, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        commissionRuleServiceStub.retrieve.reset();
        commissionRuleServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        commissionRuleServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.retrieve.called).toBeTruthy();
        expect(comp.commissionRules[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(commissionRuleServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        commissionRuleServiceStub.retrieve.reset();
        commissionRuleServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(commissionRuleServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.commissionRules[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        commissionRuleServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCommissionRule();
        await comp.$nextTick(); // clear components

        // THEN
        expect(commissionRuleServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(commissionRuleServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
