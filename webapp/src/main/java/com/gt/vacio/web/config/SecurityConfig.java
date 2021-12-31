package com.gt.vacio.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gt.vacio.web.CustomAuthenticationProvider;
import com.gt.vacio.web.model.usuarios.UserRol;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Autowired
	MySimpleUrlAuthenticationSuccessHandler myAuthenticationSuccessHandler;

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.cors()
					.and()
					.csrf()
					.disable()
					.antMatcher("/ws/**")
					.authorizeRequests()
					.anyRequest().hasRole(UserRol.WEBSERVICES.name())
					.and()
					.httpBasic();
		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// form login
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry seguridad = http
				.authorizeRequests().antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll();

		seguridad = seguridad.antMatchers("/", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
				.permitAll();

		seguridad = seguridad.antMatchers("/", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
				.permitAll();

		seguridad = seguridad.antMatchers("/resources/images/**")
				.permitAll();

		for (Map.Entry<String, Set<String>> folderPerm : UserRol.getPermissionsMap().entrySet()) {

			String[] perms = folderPerm.getValue().toArray(new String[] {});

			Logger.getLogger(getClass().getName()).log(Level.INFO, "permitiendo acceso a '" + folderPerm.getKey()
					+ " para " + Arrays.toString(perms));
			seguridad = seguridad.antMatchers(folderPerm.getKey()).hasAnyRole(perms);
		}

		seguridad.anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/login.xhtml").successHandler(myAuthenticationSuccessHandler)
				.failureUrl("/login.xhtml?authfailed=true").permitAll().and()
				.logout().logoutSuccessUrl("/login.xhtml")
				.logoutUrl("/j_spring_security_logout").and()
				.csrf().disable()
				.sessionManagement().maximumSessions(2);

		// allow to use ressource links like pdf
		http.headers().frameOptions().sameOrigin();
	}

}
