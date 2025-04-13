package com.mobile.api.security.custom;

import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.jpa.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new CustomUserDetails(user.getId(), userRepository);
    }
}
