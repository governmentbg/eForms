package com.bulpros.integrations.regix.configuration;

import com.bulpros.integrations.regix.model.client.RegixClient;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.security.KeyStore;

@Configuration
public class RegixConfiguration {

    @Value("${com.bulpros.regix.wsdl}")
    private URL regixWsdl;
    @Value("${com.bulpros.regix.keyStore.file}")
    private FileSystemResource regixKeystoreFile;
    @Value("${com.bulpros.regix.keyStore.pass}")
    private String regixKeystorePass;
    @Value("${com.bulpros.regix.keyStore.type}")
    private String regixKeystoreType;

    @Bean
    public RegixClient regixClient() throws Exception {
        return RegixClient.create(regixWsdl, regixKeystoreFile, regixKeystorePass);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HttpClient regix() throws Exception {
        return createHttpClient(regixKeystoreFile, regixKeystorePass, regixKeystoreType);
    }

    private CloseableHttpClient createHttpClient(Resource keyStoreResource, String keyStorePassword,
                                                 String regixKeystoreType) throws Exception {

        KeyStore keyStore = KeyStore.getInstance(regixKeystoreType);
        keyStore.load(keyStoreResource.getInputStream(), keyStorePassword.toCharArray());

        SSLContext sslContext = new SSLContextBuilder()
                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                //.loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                //.register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setSSLSocketFactory(sslConnectionFactory);
        builder.setConnectionManager(connManager);
        return builder.build();
    }
}
