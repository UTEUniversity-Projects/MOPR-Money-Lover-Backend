package com.mobile.api.security.custom;

import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
