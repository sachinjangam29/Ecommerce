package com.javaprojects.discovery_service.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${spring.security.user.name}")
	private String eurekaUsername;
	
	@Value("${spring.security.user.password}")
	private String eurekaPassword;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername(eurekaUsername)
				.password("{noop}"+eurekaPassword)
				.authorities("User")
				.build();
		
		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
			.authorizeHttpRequests((request) -> request.anyRequest().authenticated())
			.httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
	}
}
