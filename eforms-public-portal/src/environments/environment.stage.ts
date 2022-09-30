import {} from 'dotenv';

export const environment = {
  environment: 'stage',
  production: true,
  apiUrl: 'https://eforms-web-test.egov.bg/api',
  stsServer: 'https://idp-eforms-web-test.egov.bg',
  clientId: 'eforms-portal',
  defaultLanguage: 'BG',
  formioBaseProject: '60929223b1258f297e4bb85b',
  defaultIdP: 'eauth2-test',
  egovBaseURL: 'testportal.egov.bg',
  edeliveryURL: 'edelivery-test-v2.egov.bg',
  payEgovURL: 'pay-test.egov.bg',
  formIoUrl: 'https://forms-eforms-web-test.egov.bg',
  processEngineUrl: 'http://processes-eforms-web-test.egov.bg',
  autosaveEveryNMinutes: 5,
  ePaymentLoginUrl: 'https://pay-test.egov.bg/Account/EAuth',
  keycloak: 'https://idp-eforms-web-test.egov.bg',
  redashUrl: 'https://reports-eforms-web-test.egov.bg'
};