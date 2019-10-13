package com.lp.BOBService.security;

import java.util.Arrays;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration

@EnableWebSecurity(debug = false)
@ComponentScan("com.lp.BOBService.security")

@PropertySources({ @PropertySource("classpath:auth0.properties") })

public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        // 10.254.21.70:8080
        // System.setProperty("https.proxyHost", "10.254.21.70");
        /// System.setProperty("https.proxyPort", "8080");

        http.cors();
        JwtWebSecurityConfigurer.forRS256(apiAudience, issuer).configure(http).authorizeRequests()
                .antMatchers("/ume/public/**").permitAll().antMatchers("/ume/api/**").authenticated()
                .antMatchers("/ume/api-scoped/**").hasAuthority("update:ume");

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("/**"));
        configuration.setAllowedMethods(Arrays.asList("/**"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}