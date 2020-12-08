package com.github.natezhengbne.toolbox.security;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserCredentials {

    String token;

    public OAuth2AuthenticationToken getAuthenticationToken() {
        return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAttributeValue(String attributeName) {
        OAuth2AuthenticationToken oauthToken = getAuthenticationToken();
        if(oauthToken == null){
            return getValueFromToken(attributeName);
        } else {
            return (String) oauthToken.getPrincipal().getAttributes().get(attributeName);
        }
    }

    public String getCompany() {
        return getAttributeValue("company");
    }

    public String getDivision() {
        return getAttributeValue("division");
    }

    public String getUser() {
        return getAttributeValue("user");
    }

    public String getToken() {
        OAuth2AuthenticationToken oauthToken = getAuthenticationToken();
        if(oauthToken!=null) {
            OAuth2AccessToken accessToken = (OAuth2AccessToken) oauthToken.getDetails();
            return accessToken.getValue();
        } else {
            return token;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String getValueFromToken(String attributeName) {
        ObjectMapper objectMapper = new ObjectMapper();
        Jwt jwt = JwtHelper.decode(token);

        Map<String, Object> claims = null;
        try {
             claims = objectMapper.readValue(jwt.getClaims(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String) claims.get(attributeName);
    }
}