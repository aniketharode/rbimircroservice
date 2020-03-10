package com.zual.ZuulService.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//http.csrf().disable();
		http
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(env.getProperty("api.h2console.url.path")).permitAll()
		.antMatchers(HttpMethod.POST,env.getProperty("api.registration.url.path")).permitAll()
		.antMatchers(HttpMethod.POST,env.getProperty("api.login.url.path")).permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(new AuthrizationFilter(authenticationManager(), env));
		
	}

}
