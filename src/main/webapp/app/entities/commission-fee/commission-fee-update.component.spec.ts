import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CommissionFeeUpdate from './commission-fee-update.vue';
import CommissionFeeService from './commission-fee.service';
import AlertService from '@/shared/alert/alert.service';

import LicenseService from '@/entities/license/license.service';

type CommissionFeeUpdateComponentType = InstanceType<typeof CommissionFeeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const commissionFeeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CommissionFeeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CommissionFee Management Update Component', () => {
    let comp: CommissionFeeUpdateComponentType;
    let commissionFeeServiceStub: SinonStubbedInstance<CommissionFeeService>;

    beforeEach(() => {
      route = {};
      commissionFeeServiceStub = sinon.createStubInstance<CommissionFeeService>(CommissionFeeService);
      commissionFeeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          commissionFeeService: () => commissionFeeServiceStub,
          licenseService: () =>
            sinon.createStubInstance<LicenseService>(LicenseService, {
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
        const wrapper = shallowMount(CommissionFeeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionFee = commissionFeeSample;
        commissionFeeServiceStub.update.resolves(commissionFeeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.update.calledWith(commissionFeeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        commissionFeeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CommissionFeeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.commissionFee = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commissionFeeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        commissionFeeServiceStub.find.resolves(commissionFeeSample);
        commissionFeeServiceStub.retrieve.resolves([commissionFeeSample]);

        // WHEN
        route = {
          params: {
            commissionFeeId: `${commissionFeeSample.id}`,
          },
        };
        const wrapper = shallowMount(CommissionFeeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.commissionFee).toMatchObject(commissionFeeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        commissionFeeServiceStub.find.resolves(commissionFeeSample);
        const wrapper = shallowMount(CommissionFeeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
