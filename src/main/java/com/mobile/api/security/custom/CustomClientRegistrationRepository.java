package com.mobile.api.security.custom;

import com.mobile.api.constant.JwtConstant;
import com.mobile.api.security.jwt.JwtProperties;
import com.mobile.api.security.jwt.JwtUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomClientRegistrationRepository implements ClientRegistrationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public CustomClientRegistrationRepository(JdbcTemplate jdbcTemplate, JwtUtils jwtUtils, JwtProperties jwtProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM oauth2_registered_client WHERE client_id = ?", registrationId
        );

        if (rowSet.next()) {
            List<String> scopes = jwtUtils.parseList(rowSet.getString("scopes"));
            List<AuthorizationGrantType> grantTypes = jwtUtils.parseGrantTypes(rowSet.getString("authorization_grant_types"));

            ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(rowSet.getString("client_id"))
                    .clientId(rowSet.getString("client_id"))
                    .clientSecret(rowSet.getString("client_secret"))
                    .redirectUri(jwtProperties.getBaseUrl() + jwtUtils.parseList(rowSet.getString("redirect_uris")).stream().findFirst().orElse(null))
                    .scope(scopes.toArray(new String[0]))
                    .authorizationUri(jwtProperties.getBaseUrl() + JwtConstant.OAUTH2_URI_AUTHORIZATION)
                    .tokenUri(jwtProperties.getBaseUrl() + JwtConstant.OAUTH2_URI_TOKEN);

            grantTypes.forEach(builder::authorizationGrantType);

            return builder.build();
        }

        return null;
    }
}
