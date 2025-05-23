import { vitest } from 'vitest';
import { type ComponentMountingOptions, shallowMount } from '@vue/test-utils';
import { createTestingPinia } from '@pinia/testing';
import axios from 'axios';
import sinon from 'sinon';

import Activate from './activate.vue';

type ActivateComponentType = InstanceType<typeof Activate>;

const route = { query: { key: 'key' } };

vitest.mock('vue-router', () => ({
  useRoute: () => route,
}));

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
};

describe('Activate Component', () => {
  let activate: ActivateComponentType;
  let mountOptions: ComponentMountingOptions<ActivateComponentType>;

  beforeEach(() => {
    mountOptions = {
      global: {
        plugins: [createTestingPinia()],
      },
    };
  });

  afterAll(() => {
    sinon.restore();
  });

  it('should display error when activation fails', async () => {
    axiosStub.get.rejects({});

    const wrapper = shallowMount(Activate as any, mountOptions);
    activate = wrapper.vm;
    await activate.$nextTick();

    expect(activate.error).toBeTruthy();
    expect(activate.success).toBeFalsy();
  });

  it('should display success when activation succeeds', async () => {
    axiosStub.get.resolves({});

    const wrapper = shallowMount(Activate as any, mountOptions);
    activate = wrapper.vm;
    await activate.$nextTick();

    expect(activate.error).toBeFalsy();
    expect(activate.success).toBeTruthy();
  });
});
