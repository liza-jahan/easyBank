package com.example.easybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class projectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       // 1/ http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
       // 1/http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/my-account","/my-balance").authenticated()
                        .requestMatchers("/notice","/error").permitAll());
           http.formLogin(withDefaults());
         http.httpBasic(withDefaults());

         //2/ http.formLogin(AbstractHttpConfigurer::disable);
        //2/http.httpBasic(fcl-> fcl.disable());

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//      UserDetails user=  User.withUsername("user")
//                .password("EasyByte@12345")
//                .authorities("read")
//                .build();
//        UserDetails admin=  User.withUsername("admin")
//                .password("EasyByte@12345")
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
    public PasswordEncoder passwordEncoder(){
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){

        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
