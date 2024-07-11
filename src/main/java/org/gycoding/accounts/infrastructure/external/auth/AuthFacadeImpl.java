package org.gycoding.accounts.infrastructure.external.auth;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.JWT;
import com.auth0.net.Request;
import kong.unirest.json.JSONObject;
import org.gycoding.accounts.infrastructure.external.unirest.UnirestFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private AuthAPI authAPI;

    private String mainDomain;

    private String managementURL;
    private String managementClientId;
    private String managementClientSecret;
    private String managementTokenURL;

    public AuthFacadeImpl(
            @Value("${auth0.main.domain}") String mainDomain,
            @Value("${auth0.management.url}") String managementURL,
            @Value("${auth0.management.clientId}") String managementClientId,
            @Value("${auth0.management.clientSecret}") String managementClientSecret,
            @Value("${auth0.management.token.url}") String managementTokenURL
    ) {
        this.mainDomain             = mainDomain;

        this.managementURL          = managementURL;
        this.managementClientId     = managementClientId;
        this.managementClientSecret = managementClientSecret;
        this.managementTokenURL     = managementTokenURL;
    }

    private String getManagementToken() throws Auth0Exception {
        final var url   = this.managementTokenURL;
        final var body  = String.format(
                "{ \"client_id\": \"%s\", \"client_secret\": \"%s\", \"audience\": \"%s\", \"grant_type\": \"%s\" }",
                this.managementClientId,
                this.managementClientSecret,
                this.managementURL,
                "client_credentials"
        );

        final var response          = UnirestFacade.post(url, body);
        JSONObject jsonResponse     = new JSONObject(response.getBody());

        return jsonResponse.getString("access_token");
    }

    @Override
    public Map<String, Object> getMetadata(String userId) throws Auth0Exception {
        final var managementAPI = new ManagementAPI(this.mainDomain, this.getManagementToken());
        User user               = managementAPI.users().get(userId, null).execute();

        return user.getUserMetadata();
    }

    @Override
    public void updateMetadata(String userId, Map<String, Object> metadata) throws Auth0Exception {
        final var managementAPI      = new ManagementAPI(this.mainDomain, this.getManagementToken());
        final var user               = new User();

        user.setUserMetadata(metadata);

        Request<User> request        = managementAPI.users().update(userId, user);

        request.execute();
    }

    @Override
    public String decode(String jwt) {
        final var decodedJWT = JWT.decode(jwt.replace("Bearer ", ""));
        return decodedJWT.getClaim("sub").asString();
    }
}
