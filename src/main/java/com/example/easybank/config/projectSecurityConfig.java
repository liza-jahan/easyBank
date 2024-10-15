package com.example.easybank.config;

import com.example.easybank.exceptionhandling.CustomAccessDeniedHandler;
import com.example.easybank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class projectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 1/ http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // 1/http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))

                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())//only http
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) -> requests.requestMatchers("/my-account", "/my-balance").authenticated()
                        .requestMatchers("/notice", "/error", "/register","/invalidSession").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        //  http.exceptionHandling(ehc-> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));// It is an Global Config
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));


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
