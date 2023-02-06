package com.edix.apirestblog.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.edix.apirestblog.security.CustomUserDetailsService;
import com.edix.apirestblog.security.JwtAuthenticationEntryPoint;
import com.edix.apirestblog.security.JwtAuthenticationFilter;

/*
 * EnabledWebSecurity -> para crear reglas de seguridad propias
 * EnableGlobalMethodSecurity -> para poder redefeniri algunos de los métodos de springsecurity
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService cUdetails;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthEntryPoint;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Bean
	PasswordEncoder pEnc() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET,"/api/**")
			.permitAll()
			.antMatchers("/api/auth/**")
			.permitAll()
			.anyRequest()
			.authenticated();
		
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(cUdetails).passwordEncoder(pEnc());
	}
	
	//Para tener usuarios en memoria
	/*
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails raul = User.builder().username("Raul").password(pEnc().encode("password"))
				.roles("USER").build();
		
		UserDetails admin = User.builder().username("Admin").password(pEnc().encode("admin"))
				.roles("ADMIN").build();
		
		return new InMemoryUserDetailsManager(raul,admin);
		
		
	}
	*/
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
				
		return super.authenticationManagerBean();
	}
}
