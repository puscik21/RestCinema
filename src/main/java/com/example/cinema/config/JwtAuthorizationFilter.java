package com.example.cinema.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cinema.entity.Spectator;
import com.example.cinema.entity.UserPrincipal;
import com.example.cinema.exception.RequestException;
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

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SpectatorRepository spectatorRepository) {
        super(authenticationManager);
        this.spectatorRepository = spectatorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        if (token != null) {
            String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                    .getSubject();

            if (email != null) {
                Spectator spectator = spectatorRepository.findByEmail(email)    // TODO: 6/25/2022 change to service
                        .orElseThrow(() -> new RequestException("Cannot find user with email: " + email));
                UserPrincipal principal = new UserPrincipal(spectator);
                return new UsernamePasswordAuthenticationToken(email, null, principal.getAuthorities());
            }
            return null;
        }
        return null;
    }
}