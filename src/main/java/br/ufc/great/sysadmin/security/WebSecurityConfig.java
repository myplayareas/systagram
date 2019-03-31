package br.ufc.great.sysadmin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.ufc.great.sysadmin.service.UsersService;


/**
 * Classe de configuração do Spring Security
 * @author armandosoaressousa
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {    
    @Autowired
	private UsersService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

    	http.sessionManagement()
		.maximumSessions(2)
		.expiredUrl("/sessionExpired");
	
		http.sessionManagement().invalidSessionUrl("/login");   
		
		http.sessionManagement()
		  .sessionFixation().migrateSession();
    	
    	http.authorizeRequests()                		
                .antMatchers("/bootstrap/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/sessionExpired").permitAll()
                .antMatchers("/invalidSession").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/accesscontrol/**").hasAnyAuthority("ADMIN")
                .antMatchers("/users").hasAnyAuthority("ADMIN")
                .antMatchers("/users/edit/").hasAnyAuthority("ADMIN")
                .antMatchers("/users/**").authenticated()
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .antMatchers("/dashboard/admin").hasAnyAuthority("ADMIN")
                .antMatchers("/dashboard/user").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
            .formLogin()
            	.loginPage("/login")
            	.permitAll()
            	.defaultSuccessUrl("/")
            	.failureUrl("/login?error")                                          
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("auth_code", "JSESSIONID")
                .logoutSuccessUrl("/login")
                .permitAll();   
           	    	
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(this.userService)
    	.passwordEncoder(new BCryptPasswordEncoder());
    }
}