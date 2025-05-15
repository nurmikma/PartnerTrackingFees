import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PartnerDetails from './partner-details.vue';
import PartnerService from './partner.service';
import AlertService from '@/shared/alert/alert.service';

type PartnerDetailsComponentType = InstanceType<typeof PartnerDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const partnerSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Partner Management Detail Component', () => {
    let partnerServiceStub: SinonStubbedInstance<PartnerService>;
    let mountOptions: MountingOptions<PartnerDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      partnerServiceStub = sinon.createStubInstance<PartnerService>(PartnerService);

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
          partnerService: () => partnerServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        partnerServiceStub.find.resolves(partnerSample);
        route = {
          params: {
            partnerId: `${123}`,
          },
        };
        const wrapper = shallowMount(PartnerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.partner).toMatchObject(partnerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        partnerServiceStub.find.resolves(partnerSample);
        const wrapper = shallowMount(PartnerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
