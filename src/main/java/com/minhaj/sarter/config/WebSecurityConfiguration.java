package com.minhaj.sarter.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.minhaj.sarter.ApiKeyHeaderFilter;
import com.minhaj.sarter.entity.ApiKey;
import com.minhaj.sarter.repository.ApiKeyRepository;
import com.minhaj.sarter.repository.ApiPatternRepository;


@EnableWebSecurity
@Configuration
//@Profile("secure")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);
	
	@Autowired
	private ApiKeyRepository apiKeyReposiroty;
	@Autowired
	private ApiPatternRepository apiPatternRepository;
	
	@Value("${apikeyauth.header:ApiKey}")
	private String header;
	
	@Bean
	protected UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				ApiKey key = apiKeyReposiroty.findByKey(username);
				if (key == null) {
					throw new UsernameNotFoundException("Invalid API key '" + username + "'");
				} else {
					String[] authorities = new String[key.getAuthorities().size()];
					key.getAuthorities().toArray(authorities);
					
					return new User(key.getKey(), "", key.isEnabled(), !key.isExpired(), true, !key.isLocked(), AuthorityUtils.createAuthorityList(authorities));
				}
			}
		};
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.addFilterBefore(new ApiKeyHeaderFilter(header), BasicAuthenticationFilter.class)
			.userDetailsService(userDetailsService()).httpBasic()
			.and().csrf().disable();
			
			apiPatternRepository.findAll().forEach(p -> {
				String[] a = new String[p.getAuthorities().size()];
				p.getAuthorities().toArray(a);
				try {
					http.authorizeRequests().antMatchers(p.getPattern()).hasAnyAuthority(a);
					logger.info("URL Pattern '" + p.getPattern() + "' added for authorities " + Arrays.toString(a));
				} catch (Exception e) { 
					logger.error(e.toString());
				}
			});
			
		http
			.authorizeRequests()
			.antMatchers("/swagger-ui.html", "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-resources") // Permit swagger-ui.html page
			.permitAll()
			.anyRequest()
			.fullyAuthenticated();
	}
}

//@EnableWebSecurity
//@Configuration
//@Profile("!secure")
//class NoWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable();
//	}
//}
