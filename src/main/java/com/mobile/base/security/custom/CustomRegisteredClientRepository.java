package com.mobile.base.security.custom;

import com.mobile.base.security.jwt.JwtProperties;
import com.mobile.base.security.jwt.JwtUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRegisteredClientRepository implements RegisteredClientRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JwtUtils jwtUtils;
    private final JdbcRegisteredClientRepository delegate;
    private final JwtProperties jwtProperties;

    public CustomRegisteredClientRepository(JdbcTemplate jdbcTemplate, JwtUtils jwtUtils, JwtProperties jwtProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.delegate = new JdbcRegisteredClientRepository(jdbcTemplate);
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        delegate.save(registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM oauth2_registered_client WHERE client_id = ?", clientId);

        if (rowSet.next()) {
            List<String> scopes = jwtUtils.parseList(rowSet.getString("scopes"));
            List<AuthorizationGrantType> grantTypes = jwtUtils.parseGrantTypes(rowSet.getString("authorization_grant_types"));

            RegisteredClient registeredClient = delegate.findByClientId(clientId);

            if (registeredClient != null) {
                RegisteredClient.Builder clientBuilder = RegisteredClient.withId(rowSet.getString("id"))
                        .clientId(rowSet.getString("client_id"))
                        .clientSecret(rowSet.getString("client_secret"))
                        .redirectUri(jwtProperties.getBaseUrl() + jwtUtils.parseList(rowSet.getString("redirect_uris")).stream().findFirst().orElse(null))
                        .scopes(scope -> scope.addAll(scopes))
                        .tokenSettings(jwtUtils.parseJsonToTokenSettings(rowSet.getString("token_settings")))
                        .clientSettings(jwtUtils.parseJsonToClientSettings(rowSet.getString("client_settings")));

                grantTypes.forEach(clientBuilder::authorizationGrantType);

                return clientBuilder.build();
            }
        }

        return null;
    }
}
