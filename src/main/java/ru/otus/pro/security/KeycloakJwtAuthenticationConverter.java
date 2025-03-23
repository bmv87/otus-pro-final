package ru.otus.pro.security;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@AllArgsConstructor
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private List<String> clientIds;

    @Override
    @Nonnull
    public AbstractAuthenticationToken convert(Jwt source) {
        return new JwtAuthenticationToken(source, Stream.concat(new JwtGrantedAuthoritiesConverter().convert(source)
                        .stream(), extractResourceRoles(source).stream())
                .collect(Collectors.toSet()));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        if (!jwt.hasClaim("resource_access")) {
            return Collections.emptySet();
        }
        var resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var resourceRoles = new ArrayList<>();
        for (var id : clientIds) {
            if (resourceAccess.containsKey(id)) {
                try {
                    Map<?, ?> resource = (Map<?, ?>) resourceAccess.get(id);
                    if (resource.containsKey("roles")) {
                        resourceRoles.addAll((List<?>) resource.get("roles"));
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
        return resourceRoles.isEmpty()
                ? Collections.emptySet()
                : resourceRoles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toSet());
    }
}