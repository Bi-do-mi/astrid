package com.bidomi.astrid.Security;

import com.bidomi.astrid.Repositories.UserRepository;
import com.bidomi.astrid.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AstridAuthenticationEntryPoint authenticationEntryPoint;
    @Value("${serverLocDir}")
    private String serverLocDir;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
//                .csrf().disable()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().httpBasic()
//               disable browser login prompt
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll().deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().authorizeRequests()
                .antMatchers(
                        ("/index.html"),
                        ("/rest/users/sign_up"),
                        ("/rest/users/sign_in"),
                        ("/rest/users/all"),
                        ("/rest/users/hello"),
                        ("/rest/users/name_check"),
                        ("/home"),
                        ("/logout"),
                        ("/rest/users/enable_user"),
                        ("/rest/users/set_user_token"),
                        ("/rest/users/change_password"),
                        ("/rest/search/on_moveend"),
                        ("/rest/geo/create-markers"),
                        ("/rest/search/get_owner"),
                        ("/rest/units/get_units_images"),
                        ("/rest/users/get_users_image"),
                        ("/rest/search/on_recheck_other_units"),
                        ("/rest/search/on_recheck_oun_units")
                )
                .permitAll()
                .anyRequest().authenticated()
                .and().rememberMe().alwaysRemember(true)
                .key("AppKeyAsrtid")
                .tokenValiditySeconds(604800) // 1 week = 604800
                .tokenRepository(persistentTokenRepository());
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
