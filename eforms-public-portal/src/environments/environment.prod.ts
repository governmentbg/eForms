import {} from 'dotenv';

export const environment = {
  production: true,
  apiUrl: 'https://eforms-test.egov.bg/api',
  stsServer: 'https://keycloak-eforms-test.egov.bg/auth/realms/eforms',
  clientId: 'eforms-portal',
  defaultLanguage: 'bg',
  formioBaseProject: '60929223b1258f297e4bb85b',
  defaultIdP: 'eauth2',
  egovBaseURL: 'egov.bg',
  edeliveryURL: 'edelivery.egov.bg',
  payEgovURL: 'pay.egov.bg'
};
