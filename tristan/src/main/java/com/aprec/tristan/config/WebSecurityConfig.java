package com.aprec.tristan.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.aprec.tristan.user.auth.CustomAuthenticationFailureHandler;
import com.aprec.tristan.user.oauth2.GitHubUserService;

@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Resource
	private final UserDetailsService userDetailsService;
	
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http//.csrf().disable()
            .authorizeRequests()
            .and().formLogin()
        		.loginPage("/login")
        		.failureHandler(this.authenticationFailureHandler())
        		.defaultSuccessUrl("/index")
        		.permitAll()
        		.and()
        		.rememberMe()
        	.and().sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        	.oauth2Login()
        		.loginPage("/oauth_login")
        		.defaultSuccessUrl("/index")
        		.userInfoEndpoint(userInfo -> userInfo
        				.userService(oAuth2UserService()))
        	.and().logout()
        		.logoutUrl("/logout")
        		.clearAuthentication(true)
        		.deleteCookies("JSESSIONID", "remember-me")
        		.invalidateHttpSession(true)
        		.logoutSuccessUrl("/index")
            ;
        return http.build();
    }
	
    
    @Bean
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
    	return new GitHubUserService();
    }

    
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
    	return auth.getAuthenticationManager();
    }
	
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
		DaoAuthenticationProvider provider =
				new DaoAuthenticationProvider();
		provider.setPasswordEncoder(bCryptPasswordEncoder);
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
	
	@Bean
    SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
	
	
}
