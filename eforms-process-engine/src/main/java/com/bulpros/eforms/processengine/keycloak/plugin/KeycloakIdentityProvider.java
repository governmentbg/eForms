package com.bulpros.eforms.processengine.keycloak.plugin;

import com.bulpros.bpm.extension.keycloak.plugin.KeycloakIdentityProviderPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


  @Component 
  @ConfigurationProperties(prefix="plugin.identity.keycloak") public class
  KeycloakIdentityProvider extends KeycloakIdentityProviderPlugin {
  
  }
 