package com.example.revitaclinic.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KeycloakService {

    private static final String REALM = "ReVitaClinic";
    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloakAdminClient) {
        this.keycloak = keycloakAdminClient;
    }


    public UserRepresentation getUser(UUID kcId) {
        UserResource res = keycloak.realm(REALM)
                .users()
                .get(kcId.toString());
        return res.toRepresentation();
    }

    public List<String> getUserRoles(UUID kcId) {
        return keycloak.realm(REALM)
                .users()
                .get(kcId.toString())
                .roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
    }

    public void assignRealmRole(UUID kcId, String roleName) {
        RealmResource realm = keycloak.realm(REALM);
        RoleRepresentation role = realm.roles()
                .get(roleName)
                .toRepresentation();
        realm.users()
                .get(kcId.toString())
                .roles()
                .realmLevel()
                .add(List.of(role));
    }
}