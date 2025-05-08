import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionRuleSetUpdate from './commission-rule-set-update.vue';
import CommissionRuleSetService from './commission-rule-set.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionRuleSetUpdateComponentType = InstanceType<typeof CommissionRuleSetUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionRuleSetSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CommissionRuleSetUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CommissionRuleSet Management Update Component', () => {
    let comp: CommissionRuleSetUpdateComponentType;
    let commissionRuleSetServiceStub: SinonStubbedInstance<CommissionRuleSetService>;

    beforeEach(() => {
      route = {};
      commissionRuleSetServiceStub = sinon.createStubInstance<CommissionRuleSetService>(CommissionRuleSetService);
      commissionRuleSetServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          commissionRuleSetService: () => commissionRuleSetServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CommissionRuleSetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionRuleSet = commissionRuleSetSample;
        commissionRuleSetServiceStub.update.resolves(commissionRuleSetSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.update.calledWith(commissionRuleSetSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        commissionRuleSetServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CommissionRuleSetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionRuleSet = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionRuleSetServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        commissionRuleSetServiceStub.find.resolves(commissionRuleSetSample);
        commissionRuleSetServiceStub.retrieve.resolves([commissionRuleSetSample]);

        // WHEN
        route = {
          params: {
            commissionRuleSetId: `${commissionRuleSetSample.id}`,
          },
        };
        const wrapper = shallowMount(CommissionRuleSetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.commissionRuleSet).toMatchObject(commissionRuleSetSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionRuleSetServiceStub.find.resolves(commissionRuleSetSample);
        const wrapper = shallowMount(CommissionRuleSetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
