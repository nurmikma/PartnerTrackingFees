import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionRuleUpdate from './commission-rule-update.vue';
import CommissionRuleService from './commission-rule.service';
import AlertService from '@/shared/alert/alert.service';

import CommissionRuleSetService from '@/entities/commission-rule-set/commission-rule-set.service';

type CommissionRuleUpdateComponentType = InstanceType<typeof CommissionRuleUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionRuleSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CommissionRuleUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CommissionRule Management Update Component', () => {
    let comp: CommissionRuleUpdateComponentType;
    let commissionRuleServiceStub: SinonStubbedInstance<CommissionRuleService>;

    beforeEach(() => {
      route = {};
      commissionRuleServiceStub = sinon.createStubInstance<CommissionRuleService>(CommissionRuleService);
      commissionRuleServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          commissionRuleService: () => commissionRuleServiceStub,
          commissionRuleSetService: () =>
            sinon.createStubInstance<CommissionRuleSetService>(CommissionRuleSetService, {
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
        const wrapper = shallowMount(CommissionRuleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionRule = commissionRuleSample;
        commissionRuleServiceStub.update.resolves(commissionRuleSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.update.calledWith(commissionRuleSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        commissionRuleServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CommissionRuleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionRule = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionRuleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        commissionRuleServiceStub.find.resolves(commissionRuleSample);
        commissionRuleServiceStub.retrieve.resolves([commissionRuleSample]);

        // WHEN
        route = {
          params: {
            commissionRuleId: `${commissionRuleSample.id}`,
          },
        };
        const wrapper = shallowMount(CommissionRuleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.commissionRule).toMatchObject(commissionRuleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionRuleServiceStub.find.resolves(commissionRuleSample);
        const wrapper = shallowMount(CommissionRuleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
