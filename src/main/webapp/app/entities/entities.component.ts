import { defineComponent, provide } from 'vue';

import PartnerService from './partner/partner.service';
import ClientService from './client/client.service';
import LicenseService from './license/license.service';
import InvoiceService from './invoice/invoice.service';
import InvoiceLineService from './invoice-line/invoice-line.service';
import CommissionRuleService from './commission-rule/commission-rule.service';
import CommissionRuleSetService from './commission-rule-set/commission-rule-set.service';
import CommissionFeeService from './commission-fee/commission-fee.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('partnerService', () => new PartnerService());
    provide('clientService', () => new ClientService());
    provide('licenseService', () => new LicenseService());
    provide('invoiceService', () => new InvoiceService());
    provide('invoiceLineService', () => new InvoiceLineService());
    provide('commissionRuleService', () => new CommissionRuleService());
    provide('commissionRuleSetService', () => new CommissionRuleSetService());
    provide('commissionFeeService', () => new CommissionFeeService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
