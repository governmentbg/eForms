package com.bulpros.integrations.regix.model.client;

import com.bulpros.integrations.regix.model.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.core.io.Resource;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class RegixClient {

    private final IRegiXEntryPointV2 regix;

    public static RegixClient create(URL wsdlLocation, Resource keyStore, String keyStorePassword) throws Exception {
        RegixClient client = new RegixClient(new RegiXEntryPointV2(wsdlLocation).getWSHttpBindingIRegiXEntryPointV2(new AddressingFeature()));
        client.initTLS(keyStore, keyStorePassword);
        return client;
    }

    public static ServiceRequestData createRequestData(Operation operation, Object requestBody) {
        ServiceRequestData request = new ServiceRequestData();
        request.setOperation(operation.getKey());
        ServiceRequestData.Argument arg = new ServiceRequestData.Argument();
        arg.setAny(requestBody);
        request.setArgument(arg);
        return request;
    }

    public RegixClient(IRegiXEntryPointV2 regix) {
        this.regix = regix;
    }

    public void initTLS(Resource keyStore, String keyStorePassword) throws Exception {
        HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(regix).getConduit();
        TLSClientParameters tlsClientParameters = new TLSClientParameters();
        tlsClientParameters.setDisableCNCheck(false);
        tlsClientParameters.setUseHttpsURLConnectionDefaultSslSocketFactory(false);
        conduit.setTlsClientParameters(createSocketFactory(tlsClientParameters, keyStore.getInputStream(), keyStorePassword.toCharArray()));
        BindingProvider bindingProvider = (BindingProvider) regix;
        bindingProvider.getRequestContext().put("ws-security.disable.require.client.cert.check", true);
    }

    public ServiceResultData execute(ServiceRequestData serviceRequestData) {
        RequestWrapper requestWrapper = new RequestWrapper();
        requestWrapper.setServiceRequestData(serviceRequestData);
        return regix.execute(requestWrapper).getServiceResultData();
    }

    private TLSClientParameters createSocketFactory(TLSClientParameters tlsClientParameters, InputStream keyStoreStream,
                                                    char[] keyStorePassword) throws Exception {
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
    }
}
