package com.gestvicole.gestionavicole.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gestvicole.gestionavicole.security.CustomUserDetailsService;
import com.gestvicole.gestionavicole.security.SecurityConstants;
import com.gestvicole.gestionavicole.security.jwt.JwtTokenProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF (cross site request forgery)
        http.cors().and().csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()//
                .antMatchers(SecurityConstants.AUTH_LOGIN_URL).permitAll()//
                .antMatchers(SecurityConstants.ACCESS_DENIED).permitAll()//
                .antMatchers(SecurityConstants.USER_SAVE_URL).permitAll()//
                // .antMatchers("/h2-console/**/**").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();

        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().accessDeniedPage("/error");

        // Apply JWT
        http.apply(new AuthorizationFilterConfig(jwtTokenProvider));

        // Optional, if you want to test the API from a browser
        // http.httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public")
        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
        // .and()
        // .ignoring()
        // .antMatchers("/h2-console/**/**");
        ;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        // config.addAllowedHeader("*");
        config.setAllowedHeaders(getAllowedHeaders());
        // config.addAllowedMethod("*");
        config.setAllowedMethods(getAllowedMethods());
        source.registerCorsConfiguration("/**", config);
        // FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        CorsFilter bean = new CorsFilter(source);
        // bean.setOrder(0);
        System.out.println("*===============* GESTVICOLE REST API Config called *===============*");
        return bean;
    }

    private List<String> getAllowedHeaders() {
        // "origin, content-typeClient, accept, x-req"
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("origin");
        allowedHeaders.add("content-typeClient");
        allowedHeaders.add("accept");
        allowedHeaders.add("authorization");
        allowedHeaders.add("x-req");

        allowedHeaders.add("Origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("Accept");
        allowedHeaders.add("Authorization");
        allowedHeaders.add("X-req");

        return allowedHeaders;
    }

    private List<String> getAllowedMethods() {
        // "GET, POST, PUT, DELETE, OPTIONS"
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("GET");
        allowedHeaders.add("POST");
        allowedHeaders.add("PUT");
        allowedHeaders.add("DELETE");
        allowedHeaders.add("OPTIONS");

        return allowedHeaders;
    }
}
