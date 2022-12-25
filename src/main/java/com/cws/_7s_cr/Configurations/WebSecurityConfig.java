package com.cws._7s_cr.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
//                Cleanings
                .antMatchers("/Cleanings/Create").hasRole("ADMIN")
                .antMatchers("/Cleanings/Create/*").hasRole("ADMIN")
                .antMatchers("/Cleanings/Edit/*").hasRole("ADMIN")
                .antMatchers("/Cleanings/Delete/*").hasRole("ADMIN")
//                Employees
                .antMatchers("/Employees/Create").hasRole("ADMIN")
                .antMatchers("/Employees/Create/*").hasRole("ADMIN")
                .antMatchers("/Employees/Edit/*").hasRole("ADMIN")
                .antMatchers("/Employees/Delete/*").hasRole("ADMIN")
//                Inventories
                .antMatchers("/Inventories/Create").hasRole("ADMIN")
                .antMatchers("/Inventories/Create/*").hasRole("ADMIN")
                .antMatchers("/Inventories/Edit/*").hasRole("ADMIN")
                .antMatchers("/Inventories/Delete/*").hasRole("ADMIN")
//                IssuedInventories
                .antMatchers("/IssuedInventories/Create").hasRole("ADMIN")
                .antMatchers("/IssuedInventories/Create/*").hasRole("ADMIN")
                .antMatchers("/IssuedInventories/Edit/*").hasRole("ADMIN")
                .antMatchers("/IssuedInventories/Delete/*").hasRole("ADMIN")
//                Rooms
                .antMatchers("/Rooms/Create").hasRole("ADMIN")
                .antMatchers("/Rooms/Create/*").hasRole("ADMIN")
                .antMatchers("/Rooms/Edit/*").hasRole("ADMIN")
                .antMatchers("/Rooms/Delete/*").hasRole("ADMIN")
//                Students
                .antMatchers("/Students/Create").hasRole("ADMIN")
                .antMatchers("/Students/Create/*").hasRole("ADMIN")
                .antMatchers("/Students/Edit/*").hasRole("ADMIN")
                .antMatchers("/Students/Delete/*").hasRole("ADMIN")
//                Violations
                .antMatchers("/Violations/Create").hasRole("ADMIN")
                .antMatchers("/Violations/Create/*").hasRole("ADMIN")
                .antMatchers("/Violations/Edit/*").hasRole("ADMIN")
                .antMatchers("/Violations/Delete/*").hasRole("ADMIN")
//                Visitors
                .antMatchers("/Visitors/Create").hasRole("ADMIN")
                .antMatchers("/Visitors/Create/*").hasRole("ADMIN")
                .antMatchers("/Visitors/Edit/*").hasRole("ADMIN")
                .antMatchers("/Visitors/Delete/*").hasRole("ADMIN")
//                Any
                .anyRequest().authenticated())
            .exceptionHandling().accessDeniedPage("/login").and()
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll())
            .logout((logout) -> logout.permitAll());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("user_password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin_password"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
