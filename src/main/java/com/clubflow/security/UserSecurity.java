package com.clubflow.security;


import com.clubflow.model.User;
import com.clubflow.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    private final UserRepository userRepository;

    public UserSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return false;
        }

        String email = ((UserDetails) principal).getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        return user != null && user.getId().equals(userId);
    }
}