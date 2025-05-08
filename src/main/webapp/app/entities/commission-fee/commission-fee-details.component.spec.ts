import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionFeeDetails from './commission-fee-details.vue';
import CommissionFeeService from './commission-fee.service';
import AlertService from '@/shared/alert/alert.service';

type CommissionFeeDetailsComponentType = InstanceType<typeof CommissionFeeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionFeeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CommissionFee Management Detail Component', () => {
    let commissionFeeServiceStub: SinonStubbedInstance<CommissionFeeService>;
    let mountOptions: MountingOptions<CommissionFeeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      commissionFeeServiceStub = sinon.createStubInstance<CommissionFeeService>(CommissionFeeService);

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
          commissionFeeService: () => commissionFeeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        commissionFeeServiceStub.find.resolves(commissionFeeSample);
        route = {
          params: {
            commissionFeeId: `${123}`,
          },
        };
        const wrapper = shallowMount(CommissionFeeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.commissionFee).toMatchObject(commissionFeeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionFeeServiceStub.find.resolves(commissionFeeSample);
        const wrapper = shallowMount(CommissionFeeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
