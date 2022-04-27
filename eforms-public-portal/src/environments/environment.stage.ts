import {} from 'dotenv';

export const environment = {
  production: true,
  apiUrl: 'https://eforms-web-test.egov.bg/api',
  stsServer: 'https://idp-eforms-web-test.egov.bg/auth/realms/eforms',
  clientId: 'eforms-portal',
  defaultLanguage: 'bg',
  formioBaseProject: '60929223b1258f297e4bb85b',
  defaultIdP: 'eauth2-test',
  egovBaseURL: 'testportal.egov.bg',
  edeliveryURL: 'edelivery-test-v2.egov.bg',
  payEgovURL: 'pay-test.egov.bg'
};