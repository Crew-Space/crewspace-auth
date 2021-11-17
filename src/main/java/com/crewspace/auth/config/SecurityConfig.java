package com.crewspace.auth.config;

import com.crewspace.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import com.crewspace.auth.oauth2.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final OAuthService oAuthService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .oauth2Login()
            .userInfoEndpoint()
            .userService(oAuthService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler);

        http
            .cors().and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/auth/**", "/login/**").permitAll()
            .and()
            .formLogin().disable();


    }
}
