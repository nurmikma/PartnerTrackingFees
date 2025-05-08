import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import InvoiceService from './invoice.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Invoice } from '@/shared/model/invoice.model';

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
  describe('Invoice Service', () => {
    let service: InvoiceService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new InvoiceService();
      currentDate = new Date();
      elemDefault = new Invoice(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { invoiceDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a Invoice', async () => {
        const returnedFromService = { id: 123, invoiceDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { invoiceDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Invoice', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Invoice', async () => {
        const returnedFromService = {
          invoiceId: 'BBBBBB',
          clientId: 'BBBBBB',
          partnerId: 'BBBBBB',
          invoiceAmount: 1,
          invoiceDate: dayjs(currentDate).format(DATE_FORMAT),
          invoiceType: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { invoiceDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Invoice', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Invoice', async () => {
        const patchObject = {
          clientId: 'BBBBBB',
          partnerId: 'BBBBBB',
          invoiceDate: dayjs(currentDate).format(DATE_FORMAT),
          ...new Invoice(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { invoiceDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Invoice', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Invoice', async () => {
        const returnedFromService = {
          invoiceId: 'BBBBBB',
          clientId: 'BBBBBB',
          partnerId: 'BBBBBB',
          invoiceAmount: 1,
          invoiceDate: dayjs(currentDate).format(DATE_FORMAT),
          invoiceType: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { invoiceDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Invoice', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Invoice', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Invoice', async () => {
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
