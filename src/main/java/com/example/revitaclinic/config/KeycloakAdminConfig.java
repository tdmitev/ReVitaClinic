package com.example.revitaclinic.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username("admin")
                .password("admin123")
                .build();
    }
}

//@Configuration
//public class KeycloakAdminConfig {
//
//    @Value("${keycloak.auth-server-url}")
//    private String authServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${keycloak.resource}")
//    private String clientId;
//
//    @Value("${keycloak.credentials.secret}")
//    private String clientSecret;
//
//    @Bean
//    public Keycloak keycloakAdminClient() {
//        return KeycloakBuilder.builder()
//                .serverUrl(authServerUrl)
//                .realm("master")                       // идва от admin credentials realm
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }
//}


