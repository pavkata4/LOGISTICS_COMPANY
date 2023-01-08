package com.example.logistics.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String INDEX = "/";
    private static final String USERS_REGISTER = "/users/register";
    private static final String USERS_LOGIN = "/users/login";
    private static final String SHIPMENTS = "/all_shipments";
    private static final String MY_SHIPMENTS = "/my_shipments";
    private static final String ALL_USERS = "/all_users";
    private static final String HOME = "/home";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String USERS_LOGOUT = "/users/logout";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception  {
        httpSecurity
            .headers()
                .frameOptions()
                .sameOrigin()
            .and()
                .cors()
                    .disable()
                .csrf()
                    .disable()
                    .authorizeRequests()
                .antMatchers(HOME, SHIPMENTS, MY_SHIPMENTS, ALL_USERS)
                    .authenticated()
                .antMatchers(INDEX, USERS_LOGIN, USERS_REGISTER)
                    .anonymous()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                        .loginPage(USERS_LOGIN)
                        .usernameParameter(USERNAME)
                        .passwordParameter(PASSWORD)
                        .defaultSuccessUrl(HOME)
                .and()
                    .logout()
                        .logoutUrl(USERS_LOGOUT)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl(INDEX);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**/*.js", "/**/*.css")
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/icon/**",
                        "/scripts/**");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
