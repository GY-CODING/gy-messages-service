package org.gycoding.messages.infrastructure.external.auth;

import com.auth0.exception.Auth0Exception;
import kong.unirest.json.JSONObject;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private String mainDomain;

    private String managementURL;
    private String managementClientId;
    private String managementClientSecret;
    private String managementTokenURL;
    private String userinfoURL;

    public AuthFacadeImpl(
            @Value("${auth0.main.domain}") String mainDomain,
            @Value("${auth0.management.url}") String managementURL,
            @Value("${auth0.management.clientId}") String managementClientId,
            @Value("${auth0.management.clientSecret}") String managementClientSecret,
            @Value("${auth0.management.token.url}") String managementTokenURL,
            @Value("${auth0.userinfo.url}") String userinfoURL
    ) {
        this.mainDomain             = mainDomain;

        this.managementURL          = managementURL;
        this.managementClientId     = managementClientId;
        this.managementClientSecret = managementClientSecret;
        this.managementTokenURL     = managementTokenURL;
        this.userinfoURL            = userinfoURL;
    }

    private String getManagementToken() throws Auth0Exception {
        final var body  = String.format(
                "{ \"client_id\": \"%s\", \"client_secret\": \"%s\", \"audience\": \"%s\", \"grant_type\": \"%s\" }",
                this.managementClientId,
                this.managementClientSecret,
                this.managementURL,
                "client_credentials"
        );

        final var response          = UnirestFacade.post(this.managementTokenURL, body);
        JSONObject jsonResponse     = new JSONObject(response.getBody());

        return jsonResponse.getString("access_token");
    }

    @Override
    public String decode(String token) {
        final var headers = new HashMap<String, String>();

        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        final var response          = UnirestFacade.get(this.userinfoURL, headers);
        JSONObject jsonResponse     = new JSONObject(response.getBody());

        return jsonResponse.getString("sub");
    }
}
