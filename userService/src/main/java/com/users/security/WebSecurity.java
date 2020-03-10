package com.users.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.users.service.UsersService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	
	@Autowired
	Environment env;
	
	@Autowired
	private UsersService userservice;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		//http.csrf().disable();
		http
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.authorizeRequests()
		.antMatchers("/login").hasIpAddress(env.getProperty("gateway.ip"))
		.and()
		.addFilter(getAuthentication());
		//.formLogin()
		//.loginPage("/login");
		//.httpBasic();
		
		//http.authorizeRequests().antMatchers("/getEcb").hasAuthority("ADMIN").and().httpBasic();
		
		//http.authorizeRequests()
		
//		.and()
//		.addFilter(getAuthentication());
//		
	
		//http.headers().frameOptions().disable();
		
		//.and()
		//.csrf().ignoringAntMatchers("/h2/**");//don't apply CSRF protection to /h2-console
		
		//http.authorizeRequests().antMatchers("/h2").permitAll();
	//	super.configure(http);
	}

	private AuthenticationFiler getAuthentication() throws Exception {
		AuthenticationFiler authenticationFiler = new AuthenticationFiler(userservice,env,authenticationManager());
		//authenticationFiler.setAuthenticationManager(authenticationManager());
		authenticationFiler.setFilterProcessesUrl("/users/login");
		
		return authenticationFiler;
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		
	//	auth.authenticationProvider(authenticationProvider());
		
		auth.userDetailsService(userservice).passwordEncoder(bcrypt);
		//super.configure(auth);
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider(){
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(bcrypt);
		daoAuthenticationProvider.setUserDetailsService(userservice);
		
		return daoAuthenticationProvider;
		
	}
	
}
