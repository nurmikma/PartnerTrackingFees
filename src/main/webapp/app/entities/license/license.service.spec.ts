import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import LicenseService from './license.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { License } from '@/shared/model/license.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('License Service', () => {
    let service: LicenseService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new LicenseService();
      currentDate = new Date();
      elemDefault = new License(123, 'AAAAAAA', currentDate, currentDate, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          licenseStartDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a License', async () => {
        const returnedFromService = {
          id: 123,
          licenseStartDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { licenseStartDate: currentDate, licenseEndDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a License', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a License', async () => {
        const returnedFromService = {
          licenseRuleName: 'BBBBBB',
          licenseStartDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseEndDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseQuantity: 1,
          pricePerLicense: 1,
          totalLicenseAmount: 1,
          ...elemDefault,
        };

        const expected = { licenseStartDate: currentDate, licenseEndDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a License', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a License', async () => {
        const patchObject = {
          licenseRuleName: 'BBBBBB',
          licenseStartDate: dayjs(currentDate).format(DATE_FORMAT),
          totalLicenseAmount: 1,
          ...new License(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { licenseStartDate: currentDate, licenseEndDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a License', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of License', async () => {
        const returnedFromService = {
          licenseRuleName: 'BBBBBB',
          licenseStartDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseEndDate: dayjs(currentDate).format(DATE_FORMAT),
          licenseQuantity: 1,
          pricePerLicense: 1,
          totalLicenseAmount: 1,
          ...elemDefault,
        };
        const expected = { licenseStartDate: currentDate, licenseEndDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of License', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a License', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a License', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
