import {} from 'dotenv';

export const environment = {
  environment: 'production',
  production: true,
  apiUrl: 'https://eforms-test.egov.bg/api',
  stsServer: 'https://keycloak-eforms-test.egov.bg',
  clientId: 'eforms-portal',
  defaultLanguage: 'BG',
  formioBaseProject: '60929223b1258f297e4bb85b',
  defaultIdP: 'eauth2',
  egovBaseURL: 'egov.bg',
  edeliveryURL: 'edelivery.egov.bg',
  payEgovURL: 'pay.egov.bg',
  formIoUrl: 'https://forms-eforms-test.egov.bg',
  processEngineUrl: 'https://processes-eforms-web-test.egov.bg',
  autosaveEveryNMinutes: 5,
  ePaymentLoginUrl: 'https://pay.egov.bg/Account/EAuth',
  keycloak: 'https://keycloak-eforms-test.egov.bg',
  redashUrl: 'https://reports-eforms-web-test.egov.bg'
};
