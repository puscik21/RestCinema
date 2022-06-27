package com.example.cinema.service;

import com.example.cinema.entity.Spectator;
import com.example.cinema.pojo.UserPrincipal;
import com.example.cinema.repository.SpectatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final SpectatorRepository spectatorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Spectator spectator = this.spectatorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find spectator with email: " + email));
        return new UserPrincipal(spectator);
    }
}
