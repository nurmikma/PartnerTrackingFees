import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionRuleSetDetails from './commission-rule-set-details.vue';
import CommissionRuleSetService from './commission-rule-set.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionRuleSetDetailsComponentType = InstanceType<typeof CommissionRuleSetDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionRuleSetSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CommissionRuleSet Management Detail Component', () => {
    let commissionRuleSetServiceStub: SinonStubbedInstance<CommissionRuleSetService>;
    let mountOptions: MountingOptions<CommissionRuleSetDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      commissionRuleSetServiceStub = sinon.createStubInstance<CommissionRuleSetService>(CommissionRuleSetService);

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
          commissionRuleSetService: () => commissionRuleSetServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionRuleSetServiceStub.find.resolves(commissionRuleSetSample);
        route = {
          params: {
            commissionRuleSetId: `${123}`,
          },
        };
        const wrapper = shallowMount(CommissionRuleSetDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.commissionRuleSet).toMatchObject(commissionRuleSetSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionRuleSetServiceStub.find.resolves(commissionRuleSetSample);
        const wrapper = shallowMount(CommissionRuleSetDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
