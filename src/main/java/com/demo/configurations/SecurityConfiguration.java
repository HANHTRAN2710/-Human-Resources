package com.demo.configurations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.demo.services.AccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

	@Autowired
	private AccountService accountService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.cors(cor -> cor.disable())
					.csrf(cs -> cs.disable())
					.authorizeHttpRequests(auth -> {
						auth
							.requestMatchers("/**").permitAll()
//							.requestMatchers("/admin/**").hasRole("ADMIN")
						;
					})
					.formLogin(formLogin -> {
						formLogin
							.loginPage("/")
							.loginProcessingUrl("/account/process-login")
							.usernameParameter("username")
							.passwordParameter("password")
							.successHandler(new AuthenticationSuccessHandler() {
								@Override
								public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
	
									Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) authentication.getAuthorities();
									
									Map<String, String> defaultUrlFollowedByRole = new HashMap<String, String>();
									defaultUrlFollowedByRole.put("ADMIN", "/admin/dashboard");
									defaultUrlFollowedByRole.put("NHANVIEN", "/nhanvien/index");
									defaultUrlFollowedByRole.put("NHANVIENSUPPORT", "/nhanviensupport/index");
									
									String url = "";
									for(GrantedAuthority role : roles) {
										if(defaultUrlFollowedByRole.containsKey(role.getAuthority())) {
											url = defaultUrlFollowedByRole.get(role.getAuthority());
											break;
										}
									}
									response.sendRedirect(url);
								}
							})
						.failureUrl("/account/login?error")
						;
					})
					 
					.logout(logout -> {
						logout.logoutUrl("/account/logout")
								.logoutSuccessUrl("/account/login?logout");
					})
					.exceptionHandling(ex -> {
						ex.accessDeniedPage("/account/accessDenied");
					})
					.build();
					
	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
