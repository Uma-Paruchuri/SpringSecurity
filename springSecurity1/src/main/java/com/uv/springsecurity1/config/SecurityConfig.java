package com.uv.springsecurity1.config;

import com.uv.springsecurity1.exceptionHandling.CustomAccessDeniedHandler;
import com.uv.springsecurity1.exceptionHandling.customBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());

        /*
         http.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable());
        http.httpBasic(hbc -> hbc.disable());
        */

        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession")) //in actual env we need a html ui with all details here this ui is dummy
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/myAccounts","myCards").authenticated()
                .requestMatchers("/notices","/error","/register","/invalidSession").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new customBasicAuthenticationEntryPoint())); //considered only during login flow
        //http.exceptionHandling(ehc -> ehc.authenticationEntryPoint(new customBasicAuthenticationEntryPoint())); //It is a Global config (considered for during execution as well)
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    /*  Inmemory Impl
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{noop}Kil234@1").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$uKuzBLMct9qKJTA/BVFa2untCIZW5jjWmG1jYUJy8Xot82EIvThqq")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user,admin);
    }*/


    /*  JDBC Impl
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
