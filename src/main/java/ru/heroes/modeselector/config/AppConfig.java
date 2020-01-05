package ru.heroes.modeselector.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value("${users.admin}")
    private String admin;
    @Value("${users.first}")
    private String first;
    @Value("${users.second}")
    private String second;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username(this.admin)
                .password("123456")
                .roles("ADMIN")
                .build();
        UserDetails first = User.withDefaultPasswordEncoder()
                .username(this.first)
                .password("123456")
                .roles("USER")
                .build();
        UserDetails second = User.withDefaultPasswordEncoder()
                .username(this.second)
                .password("123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(admin, first, second));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/home/adduser").hasAuthority("ROLE_ADMIN")
//                .antMatchers("/games").hasAuthority("ROLE_USER")
                .antMatchers("/fail").permitAll()
                .anyRequest()
                .authenticated().and().csrf().disable()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/games")
                .usernameParameter("login")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
    }
}