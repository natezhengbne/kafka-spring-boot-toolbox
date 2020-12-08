package com.github.natezhengbne.appsupport.integration.test.kafka;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Instant;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockKeycloakUser.WithMockKeyCloakUserSecurityContextFactory.class)
@interface WithMockKeycloakUser {

    String company() default "someuser";
    String division() default "QLD";
    String user() default "METEXP";
    String token() default "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXZpc2lvbiI6IlFMRCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJyZWFkLWZvbyIsIndyaXRlIl0sImNvbXBhbnkiOiJNRVRFWFAiLCJleHAiOjE1MzgwNjM2NjEsInVzZXIiOiJqYXNvbnMiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjNjMDczOTYzLTJhNGQtNDA1OC05ZWJjLWZkNWFlMjZmOTUwYiIsImNsaWVudF9pZCI6ImRldmdsYW4tY2xpZW50In0.IoJgl7Jy5RyetpOfjaSXCvsErcnxlejdXzLeHQObMl0";

    class WithMockKeyCloakUserSecurityContextFactory implements WithSecurityContextFactory<WithMockKeycloakUser> {
        @Override
        public SecurityContext createSecurityContext(WithMockKeycloakUser keycloakUser) {
            Set<GrantedAuthority> authoritiesSet = new HashSet<>();
            authoritiesSet.add(new SimpleGrantedAuthority("SOME_ROLE"));
            Map <String, Object> claims = new HashMap<>();
            claims.put("sub", "1b74df83-43e1-47b4-9d2c-ee57f732ebcb");
            claims.put("preferred_username", keycloakUser.user());
            claims.put("company", keycloakUser.company());
            claims.put("division", keycloakUser.division());

            OidcIdToken token = new OidcIdToken(
                    "mock-token",
                    Instant.now(),
                    Instant.now().plusSeconds(1000),
                    claims );
            Authentication auth = new OAuth2AuthenticationToken(
                    new DefaultOidcUser(authoritiesSet, token),
                    authoritiesSet,
                    "1324" );

            String jwttoken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXZpc2lvbiI6IlFMRCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJyZWFkLWZvbyIsIndyaXRlIl0sImNvbXBhbnkiOiJNRVRFWFAiLCJleHAiOjE1MzgwNjM2NjEsInVzZXIiOiJqYXNvbnMiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjNjMDczOTYzLTJhNGQtNDA1OC05ZWJjLWZkNWFlMjZmOTUwYiIsImNsaWVudF9pZCI6ImRldmdsYW4tY2xpZW50In0.IoJgl7Jy5RyetpOfjaSXCvsErcnxlejdXzLeHQObMl0";

            ((OAuth2AuthenticationToken) auth).setDetails(new org.springframework.security.oauth2.common.OAuth2AccessToken() {
                @Override
                public Map<String, Object> getAdditionalInformation() {
                    return null;
                }

                @Override
                public Set<String> getScope() {
                    return null;
                }

                @Override
                public OAuth2RefreshToken getRefreshToken() {
                    return null;
                }

                @Override
                public String getTokenType() {
                    return null;
                }

                @Override
                public boolean isExpired() {
                    return false;
                }

                @Override
                public Date getExpiration() {
                    return null;
                }

                @Override
                public int getExpiresIn() {
                    return 0;
                }

                @Override
                public String getValue() {
                    return jwttoken;
                }
            });
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            return context;
        }
    }
}