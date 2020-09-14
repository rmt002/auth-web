package com.rmt002.authweb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rmt002.authweb.intercepter.HeaderIntercepter;
import com.rmt002.authweb.service.AuthService;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private AuthService authService;
	
	@Autowired
	private HeaderIntercepter intercepter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService);
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		//This accepts passwords with no encoding, this is absolutely not safe, using it just for testing JWT
		return NoOpPasswordEncoder.getInstance();
	}
	
	
	/*
	 *  Allow API calls to the health and authenticate end points because spring
	 *security blocks all end points by default
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
								.antMatchers("/authenticate").permitAll()
								.antMatchers("/health").permitAll()
								.anyRequest().authenticated()
								.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//The above line makes the application state less and removes the default spring session stuff
		
		//Inject our security intercepter into spring effectively making it state less in its authentication paradigm
		http.addFilterBefore(intercepter, UsernamePasswordAuthenticationFilter.class);
	}
	
	/*
	 * Re-add the Authentication Manager which was removed in a previous version of Spring Boot
	 */
	@Bean
	public AuthenticationManager authManagerBean() throws Exception {
		return super.authenticationManager();
	}	
}
