package br.edu.utfpr.commerceapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(
                                (request, response, ex) -> {
                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            ex.getMessage());
                                }));

        // Set permissions on endpoints
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(HttpMethod.GET, "/pessoa").permitAll()
                .requestMatchers(HttpMethod.POST, "/pessoa").permitAll()
                .requestMatchers(HttpMethod.GET, "/pessoa/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/pessoa").permitAll()
                .requestMatchers(HttpMethod.POST, "/passeio").permitAll()
                .requestMatchers(HttpMethod.GET, "/passeio").permitAll()
                    .requestMatchers(HttpMethod.GET, "/passeio/pages").permitAll()
                .requestMatchers(HttpMethod.GET, "/passeio/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/passeio/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/passeio/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/reserva").permitAll()
                .requestMatchers(HttpMethod.PUT, "/reserva/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/reserva").permitAll()
                .requestMatchers(HttpMethod.POST, "/pagamento").permitAll()
                .requestMatchers(HttpMethod.GET, "/reserva/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/reserva/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                // .requestMatchers(HttpMethod.POST, "/**").permitAll()
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // // Método ignora URL específicas
    // @Bean // Ignora URL's específicas
    // public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
    // return web -> web.ignoring().requestMatchers("/auth");
    // }

    // Used by Spring Security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
