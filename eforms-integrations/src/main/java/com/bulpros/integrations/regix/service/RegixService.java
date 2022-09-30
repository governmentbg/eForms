package com.bulpros.integrations.regix.service;

import com.bulpros.integrations.regix.model.RegixResponseData;
import com.bulpros.integrations.regix.model.RegixSearchData;
import com.bulpros.integrations.regix.model.client.RegixHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@Component("regixService")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegixService {

    private final RegixHttpClient regixClient;

    public RegixService(RegixHttpClient regixClient) {
        this.regixClient = regixClient;
    }

    public RegixResponseData search(RegixSearchData requestData) throws Exception {
        return regixClient.execute(requestData);
    }
}
