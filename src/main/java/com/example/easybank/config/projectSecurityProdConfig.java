package com.example.easybank.config;

import com.example.easybank.exceptionhandling.CustomAccessDeniedHandler;
import com.example.easybank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.easybank.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class projectSecurityProdConfig {

    //jwt
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {


        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(crs -> crs.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Arrays.asList("Authorization"));
                        configuration.setMaxAge(3600L);
                        return configuration;
                    }
                }))

                //  http
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/apiLogin", "/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new RequestValidationAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())//only http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/my-balance").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/myLoans").hasRole("USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/error", "/register", "/invalidSession","/apiLogin").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        EasyBankProdUserNamePwdAuthenticationProvider authenticationProvider =
                new EasyBankProdUserNamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }

}


//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
//
//        // 1/ http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//        // 1/http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        //   http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) //only https traffi
//
//        //**** CROs configure**//
////        http.cors(crs->crs.configurationSource(new CorsConfigurationSource() {
////            @Override
////            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
////                CorsConfiguration  configuration=new CorsConfiguration();
////                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
////                configuration.setAllowedMethods(Collections.singletonList("*"));
////                configuration.setAllowCredentials(true);
////                configuration.setAllowedHeaders(Collections.singletonList("*"));
////                configuration.setMaxAge(3600L);
////
////                return configuration;
////            }
////        }))
//
//        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
//                        .ignoringRequestMatchers("/contact", "/notices")
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new RequestValidationAfterFilter(), BasicAuthenticationFilter.class)
//                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
//                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession"
//                        ).maximumSessions(3)
//                        .maxSessionsPreventsLogin(true))
//
//                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())//only http
//                //  .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
////                        .requestMatchers("/my-account").hasAuthority("VIEWACCOUNT")
////                        .requestMatchers("/my-balance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
////                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
////                        // .requestMatchers("/my-account", "/my-balance","/myLoans").authenticated()
////                        .requestMatchers("/user").authenticated()
//
//
//                        .requestMatchers("/my-account").hasRole("USER")
//                        .requestMatchers("/my-balance").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers("/myLoans").hasRole("USER")
//                        .requestMatchers("/user").authenticated()
//                        .requestMatchers("/error", "/register", "/invalidSession").permitAll());
//        http.formLogin(withDefaults());
//        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
//        //  http.exceptionHandling(ehc-> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));// It is an Global Config
//        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));
//
//
//        //2/ http.formLogin(AbstractHttpConfigurer::disable);
//        //2/http.httpBasic(fcl-> fcl.disable());
//
//        return http.build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//      UserDetails user=  User.withUsername("user")
//                .password("EasyBytes@1234")
//                .authorities("read")
//                .build();
//        UserDetails admin=  User.withUsername("admin")
//                .password("EasyBytes@12345")
//                .authorities("admin")
//                .build();
//
//      return new InMemoryUserDetailsManager(user,admin);
//    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

