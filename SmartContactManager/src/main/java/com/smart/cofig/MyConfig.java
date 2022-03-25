package com.smart.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter{
    @Bean     
	public UserDetailsService getUserDetailService() {
    	System.out.println("This is getUserDetailService");
		return new UserServiceDetailsImpl();
	}
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	System.out.println("This is passwordEncoder");
    	return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    	System.out.println("This is authenticationProvider");
    	return daoAuthenticationProvider;
    }
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    	System.out.println("This is configure");
    	auth.authenticationProvider(authenticationProvider());
    }
    protected void configure(HttpSecurity http) throws Exception{
    	http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
    	.antMatchers("/user/**").hasRole("USER")
    	.antMatchers("/**").permitAll().and().formLogin()
    	.loginPage("/signin")
    	.loginProcessingUrl("/dologin")
    	.defaultSuccessUrl("/user/index")
    //	.failureUrl("/login-fail")
    	.and().csrf().disable();
    	System.out.println("This is configure HttpSecurity method");
    }
}
