package org.keycloak.vault;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.Config;

public class KubernetesSecretsVaultProviderFactory implements VaultProviderFactory{


    @Override
    public VaultProvider create(KeycloakSession keycloakSession) {
        return new KubernetesSecretsVaultProvider(keycloakSession.getContext().getRealm().getName());
    }

    @Override
    public void init(Config.Scope scope) {
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return KubernetesSecretsVaultProvider.ID;
    }
}