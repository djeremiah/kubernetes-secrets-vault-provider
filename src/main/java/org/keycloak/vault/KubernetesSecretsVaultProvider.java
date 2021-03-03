package org.keycloak.vault;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Secret;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Namespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

public class KubernetesSecretsVaultProvider implements VaultProvider{

    public static final String ID = "kubernetes-secrets";

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesSecretsVaultProvider.class);
    private final String realm;

    KubernetesSecretsVaultProvider(final String realm){
        LOGGER.debug("Initialized Kubernetes Secrets Vault Provider for realm {}", realm);
        this.realm = realm;
    }

    public VaultRawSecret obtainSecret(String key) {
        String[] parsedKey = key.split("__");

        try {
            ApiClient client = ClientBuilder.cluster().build();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();

            String secretName = parsedKey[0];
            String namespace = Namespaces.getPodNamespace();

            LOGGER.debug("Fetching secret {} in namespace {}", secretName, namespace);
            V1Secret secret = api.readNamespacedSecret(secretName, namespace, null, null, null);

            return DefaultVaultRawSecret.forBuffer(Optional.of(ByteBuffer.wrap(secret.getData().get(parsedKey[1]))));

        } catch (IOException | ApiException e){
            LOGGER.error("Error connecting to Kube API", e);
        }

        return DefaultVaultRawSecret.forBuffer(Optional.empty());
    }

    public void close() {
        // unused
    }
}
