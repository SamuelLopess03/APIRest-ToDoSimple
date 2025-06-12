package com.projectbackend.todosimple.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectbackend.todosimple.exceptions.GlobalExceptionHandler;
import com.projectbackend.todosimple.models.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {	
	
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
			JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new GlobalExceptionHandler());
		super.setAuthenticationManager(authenticationManager);
		this.jwtUtil = jwtUtil;
	}
	
	@Override	
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		try {
			
			User userCredentials = new ObjectMapper().readValue(
					request.getInputStream(), User.class);
						
			UsernamePasswordAuthenticationToken authToken =
		            new UsernamePasswordAuthenticationToken(userCredentials.getUsername(),
		            		userCredentials.getPassword());
						
		    return getAuthenticationManager().authenticate(authToken);
		} catch (IOException e) {
		    throw new RuntimeException("Erro ao tentar autenticar usuário", e);
		}

	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain, Authentication authentication)
			throws IOException, ServletException {			
		UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
				
		String username = userSpringSecurity.getUsername();
		String token = this.jwtUtil.generateToken(username);
		
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
	}
}
