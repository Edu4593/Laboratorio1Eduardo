package com.eduardo.laboratorio1eduardochacon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Crea usuarios en Keycloak (Admin REST API) para que puedan iniciar sesión.
 */
@Component
public class KeycloakAdminService {

    private final RestClient restClient = RestClient.create();
    private final String serverUrl;
    private final String realm;
    private final String adminUser;
    private final String adminPass;

    public KeycloakAdminService(@Value("${keycloak.server-url}") String serverUrl,
                                @Value("${keycloak.realm}") String realm,
                                @Value("${keycloak.admin-user}") String adminUser,
                                @Value("${keycloak.admin-password}") String adminPass) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.adminUser = adminUser;
        this.adminPass = adminPass;
    }

    public void crearUsuario(String username, String password, String role) {
        String bearer = "Bearer " + obtenerTokenAdmin();
        String base = serverUrl + "/admin/realms/" + realm;

        Map<String, Object> user = Map.of(
                "username", username,
                "enabled", true,
                "emailVerified", true,
                "email", username + "@laboratorio.local",
                "firstName", username,
                "lastName", username,
                "credentials", List.of(Map.of("type", "password", "value", password, "temporary", false))
        );

        // 1) crear el usuario
        URI location;
        try {
            location = restClient.post()
                    .uri(base + "/users")
                    .header(HttpHeaders.AUTHORIZATION, bearer)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user)
                    .retrieve()
                    .toBodilessEntity()
                    .getHeaders().getLocation();
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        String userId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);

        // 2) buscar el rol de realm y asignárselo
        Map<?, ?> rol = restClient.get()
                .uri(base + "/roles/{rol}", role)
                .header(HttpHeaders.AUTHORIZATION, bearer)
                .retrieve()
                .body(Map.class);

        restClient.post()
                .uri(base + "/users/{id}/role-mappings/realm", userId)
                .header(HttpHeaders.AUTHORIZATION, bearer)
                .contentType(MediaType.APPLICATION_JSON)
                .body(List.of(rol))
                .retrieve()
                .toBodilessEntity();
    }

    private String obtenerTokenAdmin() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "admin-cli");
        form.add("username", adminUser);
        form.add("password", adminPass);

        Map<?, ?> respuesta = restClient.post()
                .uri(serverUrl + "/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);
        return (String) respuesta.get("access_token");
    }
}
