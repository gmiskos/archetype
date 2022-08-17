package com.example.archetype.security;

import com.example.archetype.auth.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // to enable @preauthorize annotation
public class ApplicationSecurityConfig {

    @Bean
    public MyUserDetailsService userDetailsService() {
        MyUserDetailsService userDetailsService = new MyUserDetailsService();
        return userDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordConfig passwordConfig = new PasswordConfig();
        return passwordConfig.passwordEncoder();
    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        repository.setCookieName("X-ARCH-TOKEN");
        repository.setHeaderName("X-ARCH-TOKEN");
        repository.setParameterName("_csrf");
        return repository;
    }

    AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService());
        authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf().disable()
                /* csrf enabled
                //.csrf()
                //.csrfTokenRepository(csrfTokenRepository())
                //.and()
                 */

                .authorizeRequests()

                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationManager)
                //.httpBasic();
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                //.rememberMe()
               //     .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                //    .key("somethingverysecured")
                //.and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");

        http.headers().frameOptions().sameOrigin();

        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());

        return provider;
    }
}
