package com.example.easybank.config;

import com.example.easybank.exceptionhandling.CustomAccessDeniedHandler;
import com.example.easybank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class projectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 1/ http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // 1/http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        //   http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) //only https traffi

        //**** CROs configure**//
//        http.cors(crs->crs.configurationSource(new CorsConfigurationSource() {
//            @Override
//            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                CorsConfiguration  configuration=new CorsConfiguration();
//                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//                configuration.setAllowedMethods(Collections.singletonList("*"));
//                configuration.setAllowCredentials(true);
//                configuration.setAllowedHeaders(Collections.singletonList("*"));
//                configuration.setMaxAge(3600L);
//
//                return configuration;
//            }
//        }))

        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())//only http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/my-account", "/user","/my-balance").authenticated()
                                .requestMatchers("/notices", "/error", "/register","/invalidSession").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));

        // http.httpBasic(withDefaults());

        //2/ http.formLogin(AbstractHttpConfigurer::disable);
        //2/http.httpBasic(fcl-> fcl.disable());

        return http.build();
    }

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
