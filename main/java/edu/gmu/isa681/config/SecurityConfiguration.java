package edu.gmu.isa681.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("customPlayerDetailsService")
	UserDetailsService playerDetailsService;

	@Autowired
	CustomSuccessHandler customSuccessHandler;

	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(playerDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	    authenticationProvider.setUserDetailsService(playerDetailsService);
	    authenticationProvider.setPasswordEncoder(passwordEncoder());
	    return authenticationProvider;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 * 
	 * The configuration says that URL’s ‘/’ & ‘/home’ are not secured, anyone can access them. 
	 * URL ‘/admin/**’ can only be accessed by someone who have ADMIN role. 
	 * URL ‘/db/**’ can only be accessed by someone who have both ADMIN and DBA roles.
	 * 
	 * Method formLogin provides support for form based authentication and will generate a default form asking for player credentials. 
	 * You are allowed to configure your own login form. We will see examples for the same in subsequent posts.

<form-login 
    login-page="/login.html"
    authentication-failure-url="/login.html?status=LOGIN_FAILURE"
    default-target-url="/secure/index.html"
    always-use-default-target="false" />


    .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession( true )
        .and(); 

We have also used exceptionHandling().accessDeniedPage() which in this case will catch all 403 [http access denied] 
exceptions and display our player defined page instead of showing default HTTP 403 page [ which is not so helpful anyway].
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.authorizeRequests()
	  	//.antMatchers("/", "/welcome", "/register", "/login").permitAll()
	  	.antMatchers("/", "/welcome", "/register").permitAll()
	  	.antMatchers("/play**", "/board**","/home**").access("hasRole('ROLE_PLAYER')")
	  	.and().formLogin().loginPage("/login")
	  	//.defaultSuccessUrl("/welcome")
	  	.successHandler(customSuccessHandler)	  	
	  	.usernameParameter("ssoId").passwordParameter("password")
	  	.and().csrf()
	  	.and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}
}
