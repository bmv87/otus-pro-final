package ru.otus.pro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.otus.pro.security.CustomOAuth2AccessDeniedHandler;
import ru.otus.pro.security.KeycloakJwtAuthenticationConverter;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
@ConditionalOnProperty(
        value="authorization.jwt",
        matchIfMissing = true)
public class JwtSecurityConfig {
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomOAuth2AccessDeniedHandler();
    }

    @Value("${spring.security.jwt.role.locations}")
    private List<String> rolesLocation;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/**").permitAll();
                    registry.requestMatchers("/v3/api-docs/**",
                                    "/swagger-ui/**", "/swagger-ui.html")
                            .permitAll();
                })
                .oauth2ResourceServer(oauth -> oauth.jwt(j -> j.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter(rolesLocation))))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions.accessDeniedHandler(new CustomOAuth2AccessDeniedHandler()));

        return http.build();
    }
}