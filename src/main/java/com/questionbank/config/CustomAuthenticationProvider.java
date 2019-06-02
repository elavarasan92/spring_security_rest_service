package com.questionbank.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.questionbank.model.User;
import com.questionbank.service.QuestionBankService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private QuestionBankService questionBankService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		Object credentials = authentication.getCredentials();
		System.out.println("credentials class: " + credentials.getClass());
		if (!(credentials instanceof String)) {
			return null;
		}
		String password = credentials.toString();

		User user = questionBankService.checkUser(name, password);

		if (user == null) {
			throw new BadCredentialsException("Authentication failed for " + name);
		}

		System.out.println("name : " + name);
		System.out.println("password : " + password);
		System.out.println("Role : " + user.getRole());
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
		Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
		System.out.println("Auth : " + auth);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}