package ishift.pl.ComarchBackend.webService.configuration;



import ishift.pl.ComarchBackend.webDataModel.services.UserDetailsServiceImpl;
import ishift.pl.ComarchBackend.webService.jwt.JwtConfig;
import ishift.pl.ComarchBackend.webService.jwt.JwtTokenVerify;
import ishift.pl.ComarchBackend.webService.jwt.JwtUserNamePassAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtConfig jwtConfig;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtConfig jwtConfig) {
        this.userDetailsService = userDetailsService;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUserNamePassAuthFilter(authenticationManager(),jwtConfig))
                .addFilterAfter(new JwtTokenVerify(jwtConfig),JwtUserNamePassAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/synchro").hasRole("ADMIN")
                .antMatchers("/customers").hasRole("ADMIN")
                .antMatchers("/documents").authenticated()
                .antMatchers("/accOffice").authenticated()
                .anyRequest().authenticated();
    }

}
