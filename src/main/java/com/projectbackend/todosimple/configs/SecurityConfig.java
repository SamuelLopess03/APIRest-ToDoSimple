package com.projectbackend.todosimple.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.projectbackend.todosimple.security.JWTAuthenticationFilter;
import com.projectbackend.todosimple.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private static final String[] PUBLIC_MATCHERS = {
		"/"
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = {
		"/user",
		"/login"
	};
	
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationManager authenticationManager) throws Exception {
	    JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(
	    		authenticationManager, jwtUtil);

	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
	            .requestMatchers(PUBLIC_MATCHERS).permitAll()
	            .anyRequest().authenticated())
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    
	    return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
