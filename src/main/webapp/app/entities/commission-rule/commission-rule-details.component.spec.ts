import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionRuleDetails from './commission-rule-details.vue';
import CommissionRuleService from './commission-rule.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionRuleDetailsComponentType = InstanceType<typeof CommissionRuleDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionRuleSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CommissionRule Management Detail Component', () => {
    let commissionRuleServiceStub: SinonStubbedInstance<CommissionRuleService>;
    let mountOptions: MountingOptions<CommissionRuleDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      commissionRuleServiceStub = sinon.createStubInstance<CommissionRuleService>(CommissionRuleService);

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
          commissionRuleService: () => commissionRuleServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionRuleServiceStub.find.resolves(commissionRuleSample);
        route = {
          params: {
            commissionRuleId: `${123}`,
          },
        };
        const wrapper = shallowMount(CommissionRuleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.commissionRule).toMatchObject(commissionRuleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionRuleServiceStub.find.resolves(commissionRuleSample);
        const wrapper = shallowMount(CommissionRuleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
