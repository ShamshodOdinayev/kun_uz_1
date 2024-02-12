package com.example.config;

import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

//@Configuration
public class SpringSecurityConfig {}/*{
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication
    *//*    String password = "f913972a-9d3a-490c-ae4d-e47d281050da";
        System.out.println("User Pasword mazgi: " + password);

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}" + password)
                .roles("USER")
                .build();

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user));
        return authenticationProvider;*//*
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return Objects.equals(MD5Util.encode(rawPassword.toString()), encodedPassword);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/init/admin").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/init/**").permitAll()
                    .requestMatchers("/region/lang").permitAll()
                    .requestMatchers("/region/adm/**").hasRole("ADMIN")
                    .requestMatchers("/profile/adm/*").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated();
        });
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }


}*/
