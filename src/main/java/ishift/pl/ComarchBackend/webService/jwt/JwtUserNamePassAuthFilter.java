package ishift.pl.ComarchBackend.webService.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webService.services.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class JwtUserNamePassAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;
    private final LoginLogService loginLogService;


    public JwtUserNamePassAuthFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, LoginLogService loginLogService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.loginLogService = loginLogService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserData userData = new ObjectMapper()
                    .readValue(request.getInputStream(), UserData.class);

            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userData.getUserName(), userData.getPassword()));

            if(authentication.isAuthenticated())
                loginLogService.saveLoginEvent(userData.getUserName(), request);

            return authentication;

        } catch (IOException e) {
            //todo
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        Date expDate = jwtConfig.getTokenExpirationDate();

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(jwtConfig.authorityMapKey(), authResult.getAuthorities())
                .claim("access",authResult.getAuthorities().toArray()[1].toString())
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(jwtConfig.secretKey())
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(),
                jwtConfig.getTokenPrefix() + token);

        response.setStatus(HttpStatus.ACCEPTED.value());

        response.addHeader(jwtConfig.getExpirationDateHeader(), jwtConfig.returnLocalDateTimeaAsString(expDate));
    }
}
