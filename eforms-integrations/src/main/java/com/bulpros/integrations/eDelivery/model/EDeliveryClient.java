package com.bulpros.integrations.eDelivery.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
public class EDeliveryClient {
    private final IEDeliveryIntegrationService eDelivery;

    public static EDeliveryClient create(URL wsdlLocation, Resource keyStore, String keyStorePassword, String keystoreType) {
        EDeliveryClient client = new EDeliveryClient(new EDeliveryIntegrationService(wsdlLocation).getBasicHttpBindingIEDeliveryIntegrationService(new WebServiceFeature[]{}));
        client.configureSecurity(keyStore, keyStorePassword, keystoreType);
        return client;
    }

    public EDeliveryClient(IEDeliveryIntegrationService eDelivery) {
        this.eDelivery = eDelivery;
    }

    public IEDeliveryIntegrationService geteDelivery() {
        return eDelivery;
    }

    public void configureSecurity(Resource keyStore, String keyStorePassword, String keystoreType) {
        try {
            HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(eDelivery).getConduit();
            TLSClientParameters tlsClientParameters = new TLSClientParameters();
            tlsClientParameters.setDisableCNCheck(false);
            tlsClientParameters.setUseHttpsURLConnectionDefaultSslSocketFactory(false);
            conduit.setTlsClientParameters(createSocketFactory(tlsClientParameters, keyStore.getInputStream(), keyStorePassword.toCharArray()));
            BindingProvider bindingProvider = (BindingProvider) eDelivery;
            bindingProvider.getRequestContext().put("ws-security.disable.require.client.cert.check", true);
            Map<String, Object> ctx = bindingProvider.getRequestContext();
            Properties props = new Properties();
            props.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
            props.put("org.apache.ws.security.crypto.merlin.keystore.file", ((FileSystemResource) keyStore).getPath());
            props.put("org.apache.ws.security.crypto.merlin.keystore.password", keyStorePassword);
            props.put("org.apache.ws.security.crypto.merlin.keystore.private.password", keyStorePassword);
            props.put("org.apache.ws.security.crypto.merlin.keystore.type", keystoreType);
            props.put("org.apache.ws.security.crypto.merlin.keystore.alias", "1");
            ctx.put("action", "Signature");
            ctx.put("security.signature.properties", props);
        }  catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private TLSClientParameters createSocketFactory(TLSClientParameters tlsClientParameters, InputStream keyStoreStream, char[] keyStorePassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreStream, keyStorePassword);

            KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmFactory.init(keyStore, keyStorePassword);

            tlsClientParameters.setKeyManagers(kmFactory.getKeyManagers());

            Enumeration<String> aliases = keyStore.aliases();
            List<X509Certificate> trustedIssuers = new ArrayList<>();
            while (aliases.hasMoreElements()) {
                trustedIssuers.add((X509Certificate) keyStore.getCertificate(aliases.nextElement()));
            }
            X509Certificate[] acceptedIssuers = trustedIssuers.toArray(new X509Certificate[0]);

            TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return acceptedIssuers;
                        }
                    }
            };
            tlsClientParameters.setTrustManagers(trustManagers);
            tlsClientParameters.setSecureSocketProtocol("TLSv1.2");
            return tlsClientParameters;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }


}
