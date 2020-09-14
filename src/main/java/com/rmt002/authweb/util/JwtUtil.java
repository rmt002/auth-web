package com.rmt002.authweb.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtUtil {

	private String SECRET_KEY = "SecretKeys";

	// Extract the user's name from the token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract the expiration date from the token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract a particular portion of the token based on the param passed in
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Parse the token to find the body with all the info
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	// Check if the token is expired
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Call this method to generate a token for a newly logged in user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		// Can add all the custom payloads that you want to add into the JWT (like access levels)
		return createToken(claims, userDetails.getUsername());
	}

	// Create the token using JWTS inbuilt builder with the properties we want
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	// Call this function on every request to ensure that the token is still valid
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}
