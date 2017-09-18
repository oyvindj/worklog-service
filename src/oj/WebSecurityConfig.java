package oj;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.issuer}")
    private String issuer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
        //http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/users").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/question").permitAll()
                .antMatchers(HttpMethod.GET, "/question").authenticated()
                .antMatchers(HttpMethod.POST, "/question").authenticated()
                .antMatchers(HttpMethod.DELETE, "/question").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/quiz").permitAll()
                .antMatchers(HttpMethod.POST, "/quiz").authenticated()
                .antMatchers(HttpMethod.DELETE, "/quiz").authenticated()
                // .antMatchers(HttpMethod.OPTIONS, "/users").permitAll();
                .antMatchers(HttpMethod.GET, "/users").authenticated();

        JwtWebSecurityConfigurer .forRS256(audience, issuer) .configure(http);
    }
}
