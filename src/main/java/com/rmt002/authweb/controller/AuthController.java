package com.rmt002.authweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rmt002.authweb.model.AuthRequestDto;
import com.rmt002.authweb.model.AuthResponseDto;
import com.rmt002.authweb.service.AuthService;
import com.rmt002.authweb.util.JwtUtil;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private AuthService authService;
	@Autowired
	private JwtUtil jwtUtil;

	// Check if service is up using localhost:8090/health
	@RequestMapping({ "/health" })
	public String healthCheck() {
		return "auth-web is up";
	}

	// Create a token for a user
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createToken(@RequestBody AuthRequestDto authRequest) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect password");
		}
		final UserDetails userDetails = authService.loadUserByUsername(authRequest.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthResponseDto(jwt));
	}

	// Check if token is still valid
	@RequestMapping(value = "/authLive", method = RequestMethod.POST)
	public ResponseEntity<String> checkToken() throws Exception {
		return ResponseEntity.ok("User is still authenticated");
	}
}
