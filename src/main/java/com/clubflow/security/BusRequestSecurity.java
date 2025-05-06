package com.clubflow.security;


import com.clubflow.model.BusRequest;
import com.clubflow.model.User;
import com.clubflow.repository.BusRequestRepository;
import com.clubflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("busRequestSecurity")
public class BusRequestSecurity {

    private final BusRequestRepository busRequestRepository;
    private final UserRepository userRepository;

    public BusRequestSecurity(BusRequestRepository busRequestRepository, UserRepository userRepository) {
        this.busRequestRepository = busRequestRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long busRequestId, UserDetails userDetails) {
        BusRequest busRequest = busRequestRepository.findById(busRequestId).orElse(null);
        if (busRequest == null) {
            return false;
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return false;
        }

        return busRequest.getUser().getId().equals(user.getId());
    }
}