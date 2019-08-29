package com.lp.BOBService.spring;

import java.util.Arrays;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@ComponentScan("com.lp.BOBService.security")

@PropertySources({ @PropertySource("classpath:auth0.properties") })

public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;

    /*
     * public SecurityJavaConfig() { super();
     * SecurityContextHolder.setStrategyName(SecurityContextHolder.
     * MODE_INHERITABLETHREADLOCAL); }
     */

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.cors();
        JwtWebSecurityConfigurer.forRS256(apiAudience, issuer).configure(http).authorizeRequests()
                .antMatchers("/ume/public/**").permitAll().antMatchers("/ume/api/**").authenticated()
                .antMatchers("/ume/api-scoped/**").hasAuthority("update:ume");

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        System.out.println("---start set up 5 ----- via http");
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