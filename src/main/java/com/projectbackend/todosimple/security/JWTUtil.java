package com.projectbackend.todosimple.security;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String username) {
	    SecretKey key = getKeyBySecret();

	    return Jwts.builder()
	    	    .subject(username)  
	    	    .expiration(Date.from(Instant.now().plusSeconds(this.expiration)))
	    	    .signWith(key)
	    	    .compact();
	}
	
	public boolean isValidToken(String token) {
		Claims claims = getClaims(token);
		
		if(Objects.nonNull(claims)) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			if(Objects.nonNull(username) && Objects.nonNull(expirationDate) 
					&& now.before(expirationDate))
				return true;
		}
		
		return false;
	}
	
	
	private SecretKey getKeyBySecret() {
		SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
		
		return key;
	}
	
	private Claims getClaims(String token) {
		SecretKey key = getKeyBySecret();
		
		try {
			return Jwts.parser()
				    .verifyWith(key)
				    .build()
				    .parseSignedClaims(token)
				    .getPayload();
		} catch (Exception e) {
			return null;
		}
	}
}
