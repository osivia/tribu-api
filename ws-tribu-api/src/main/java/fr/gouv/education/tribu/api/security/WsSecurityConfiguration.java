package fr.gouv.education.tribu.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import fr.gouv.education.tribu.api.WsConfiguration;

@Configuration
@EnableWebSecurity
public class WsSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${tribu.api.key}")
	private String principalRequestValue;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		APIKeyAuthFilter filter = new APIKeyAuthFilter(WsConfiguration.AUTHORIZATION_HEADER);
		filter.setAuthenticationManager(new APIKeyAuthenticationManager(principalRequestValue));

		httpSecurity.antMatcher("/contents/**").csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).authorizeRequests()
				.anyRequest().authenticated();
	}

}
