package com.example.cinema.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.pojo.UserPrincipal;
import com.example.cinema.repository.SpectatorRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final SpectatorRepository spectatorRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SpectatorRepository spectatorRepository, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.spectatorRepository = spectatorRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(jwtProperties.getHeaderKey());
        if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(jwtProperties.getTokenPrefix(), "");
        Authentication auth = getUsernamePasswordAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(String token) {
        String email = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret().getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if (email == null) {
            return null;
        }
        Spectator spectator = spectatorRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("Cannot find user with email: " + email));
        UserPrincipal principal = new UserPrincipal(spectator);
        return new UsernamePasswordAuthenticationToken(email, null, principal.getAuthorities());
    }
}

