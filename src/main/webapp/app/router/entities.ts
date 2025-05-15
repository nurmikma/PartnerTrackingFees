import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Partner = () => import('@/entities/partner/partner.vue');
const PartnerUpdate = () => import('@/entities/partner/partner-update.vue');
const PartnerDetails = () => import('@/entities/partner/partner-details.vue');

const Client = () => import('@/entities/client/client.vue');
const ClientUpdate = () => import('@/entities/client/client-update.vue');
const ClientDetails = () => import('@/entities/client/client-details.vue');

const License = () => import('@/entities/license/license.vue');
const LicenseUpdate = () => import('@/entities/license/license-update.vue');
const LicenseDetails = () => import('@/entities/license/license-details.vue');

const Invoice = () => import('@/entities/invoice/invoice.vue');
const InvoiceUpdate = () => import('@/entities/invoice/invoice-update.vue');
const InvoiceDetails = () => import('@/entities/invoice/invoice-details.vue');

const InvoiceLine = () => import('@/entities/invoice-line/invoice-line.vue');
const InvoiceLineUpdate = () => import('@/entities/invoice-line/invoice-line-update.vue');
const InvoiceLineDetails = () => import('@/entities/invoice-line/invoice-line-details.vue');

const CommissionRule = () => import('@/entities/commission-rule/commission-rule.vue');
const CommissionRuleUpdate = () => import('@/entities/commission-rule/commission-rule-update.vue');
const CommissionRuleDetails = () => import('@/entities/commission-rule/commission-rule-details.vue');

const CommissionRuleSet = () => import('@/entities/commission-rule-set/commission-rule-set.vue');
const CommissionRuleSetUpdate = () => import('@/entities/commission-rule-set/commission-rule-set-update.vue');
const CommissionRuleSetDetails = () => import('@/entities/commission-rule-set/commission-rule-set-details.vue');

const CommissionFee = () => import('@/entities/commission-fee/commission-fee.vue');
const CommissionFeeUpdate = () => import('@/entities/commission-fee/commission-fee-update.vue');
const CommissionFeeDetails = () => import('@/entities/commission-fee/commission-fee-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'partner',
      name: 'Partner',
      component: Partner,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'partner/new',
      name: 'PartnerCreate',
      component: PartnerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'partner/:partnerId/edit',
      name: 'PartnerEdit',
      component: PartnerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'partner/:partnerId/view',
      name: 'PartnerView',
      component: PartnerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client',
      name: 'Client',
      component: Client,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/new',
      name: 'ClientCreate',
      component: ClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/:clientId/edit',
      name: 'ClientEdit',
      component: ClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/:clientId/view',
      name: 'ClientView',
      component: ClientDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'license',
      name: 'License',
      component: License,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'license/new',
      name: 'LicenseCreate',
      component: LicenseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'license/:licenseId/edit',
      name: 'LicenseEdit',
      component: LicenseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'license/:licenseId/view',
      name: 'LicenseView',
      component: LicenseDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice',
      name: 'Invoice',
      component: Invoice,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/new',
      name: 'InvoiceCreate',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/edit',
      name: 'InvoiceEdit',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/view',
      name: 'InvoiceView',
      component: InvoiceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-line',
      name: 'InvoiceLine',
      component: InvoiceLine,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-line/new',
      name: 'InvoiceLineCreate',
      component: InvoiceLineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-line/:invoiceLineId/edit',
      name: 'InvoiceLineEdit',
      component: InvoiceLineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-line/:invoiceLineId/view',
      name: 'InvoiceLineView',
      component: InvoiceLineDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule',
      name: 'CommissionRule',
      component: CommissionRule,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule/new',
      name: 'CommissionRuleCreate',
      component: CommissionRuleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule/:commissionRuleId/edit',
      name: 'CommissionRuleEdit',
      component: CommissionRuleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule/:commissionRuleId/view',
      name: 'CommissionRuleView',
      component: CommissionRuleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule-set',
      name: 'CommissionRuleSet',
      component: CommissionRuleSet,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule-set/new',
      name: 'CommissionRuleSetCreate',
      component: CommissionRuleSetUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule-set/:commissionRuleSetId/edit',
      name: 'CommissionRuleSetEdit',
      component: CommissionRuleSetUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-rule-set/:commissionRuleSetId/view',
      name: 'CommissionRuleSetView',
      component: CommissionRuleSetDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-fee',
      name: 'CommissionFee',
      component: CommissionFee,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-fee/new',
      name: 'CommissionFeeCreate',
      component: CommissionFeeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-fee/:commissionFeeId/edit',
      name: 'CommissionFeeEdit',
      component: CommissionFeeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'commission-fee/:commissionFeeId/view',
      name: 'CommissionFeeView',
      component: CommissionFeeDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
