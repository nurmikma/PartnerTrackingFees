import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PartnerUpdate from './partner-update.vue';
import PartnerService from './partner.service';
import AlertService from '@/shared/alert/alert.service';

type PartnerUpdateComponentType = InstanceType<typeof PartnerUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const partnerSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PartnerUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Partner Management Update Component', () => {
    let comp: PartnerUpdateComponentType;
    let partnerServiceStub: SinonStubbedInstance<PartnerService>;

    beforeEach(() => {
      route = {};
      partnerServiceStub = sinon.createStubInstance<PartnerService>(PartnerService);
      partnerServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          partnerService: () => partnerServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PartnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.partner = partnerSample;
        partnerServiceStub.update.resolves(partnerSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(partnerServiceStub.update.calledWith(partnerSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        partnerServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PartnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.partner = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(partnerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        partnerServiceStub.find.resolves(partnerSample);
        partnerServiceStub.retrieve.resolves([partnerSample]);

        // WHEN
        route = {
          params: {
            partnerId: `${partnerSample.id}`,
          },
        };
        const wrapper = shallowMount(PartnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.partner).toMatchObject(partnerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        partnerServiceStub.find.resolves(partnerSample);
        const wrapper = shallowMount(PartnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
