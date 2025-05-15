import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LicenseUpdate from './license-update.vue';
import LicenseService from './license.service';
import AlertService from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import PartnerService from '@/entities/partner/partner.service';
import CommissionRuleSetService from '@/entities/commission-rule-set/commission-rule-set.service';

type LicenseUpdateComponentType = InstanceType<typeof LicenseUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const licenseSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<LicenseUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('License Management Update Component', () => {
    let comp: LicenseUpdateComponentType;
    let licenseServiceStub: SinonStubbedInstance<LicenseService>;

    beforeEach(() => {
      route = {};
      licenseServiceStub = sinon.createStubInstance<LicenseService>(LicenseService);
      licenseServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          licenseService: () => licenseServiceStub,
          clientService: () =>
            sinon.createStubInstance<ClientService>(ClientService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          partnerService: () =>
            sinon.createStubInstance<PartnerService>(PartnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
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
        const wrapper = shallowMount(LicenseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.license = licenseSample;
        licenseServiceStub.update.resolves(licenseSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(licenseServiceStub.update.calledWith(licenseSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        licenseServiceStub.create.resolves(entity);
        const wrapper = shallowMount(LicenseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.license = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(licenseServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        licenseServiceStub.find.resolves(licenseSample);
        licenseServiceStub.retrieve.resolves([licenseSample]);

        // WHEN
        route = {
          params: {
            licenseId: `${licenseSample.id}`,
          },
        };
        const wrapper = shallowMount(LicenseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.license).toMatchObject(licenseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        licenseServiceStub.find.resolves(licenseSample);
        const wrapper = shallowMount(LicenseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
