package com.mobile.api.security.custom;

import com.mobile.api.security.jwt.JwtProperties;
import com.mobile.api.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public CustomRegisteredClientRepository(JdbcTemplate jdbcTemplate, JwtUtils jwtUtils, JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.delegate = new JdbcRegisteredClientRepository(jdbcTemplate);
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public void updateClientId(String id, String newClientId) {
        String sql = "UPDATE oauth2_registered_client SET client_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, newClientId, id);
    }

    @Transactional
    public void updateClientSecret(String id, String newClientSecret) {
        String sql = "UPDATE oauth2_registered_client SET client_secret = ? WHERE id = ?";
        jdbcTemplate.update(sql, newClientSecret, id);
    }
}
