import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LicenseDetails from './license-details.vue';
import LicenseService from './license.service';
import AlertService from '@/shared/alert/alert.service';

type LicenseDetailsComponentType = InstanceType<typeof LicenseDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const licenseSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('License Management Detail Component', () => {
    let licenseServiceStub: SinonStubbedInstance<LicenseService>;
    let mountOptions: MountingOptions<LicenseDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      licenseServiceStub = sinon.createStubInstance<LicenseService>(LicenseService);

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
          licenseService: () => licenseServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        licenseServiceStub.find.resolves(licenseSample);
        route = {
          params: {
            licenseId: `${123}`,
          },
        };
        const wrapper = shallowMount(LicenseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.license).toMatchObject(licenseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        licenseServiceStub.find.resolves(licenseSample);
        const wrapper = shallowMount(LicenseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
