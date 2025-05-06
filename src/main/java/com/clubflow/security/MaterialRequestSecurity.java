package com.clubflow.security;

import com.clubflow.model.MaterialRequest;
import com.clubflow.model.User;
import com.clubflow.repository.MaterialRequestRepository;
import com.clubflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("materialRequestSecurity")
public class MaterialRequestSecurity {

    private final MaterialRequestRepository materialRequestRepository;
    private final UserRepository userRepository;

    public MaterialRequestSecurity(MaterialRequestRepository materialRequestRepository, UserRepository userRepository) {
        this.materialRequestRepository = materialRequestRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long materialRequestId, UserDetails userDetails) {
        MaterialRequest materialRequest = materialRequestRepository.findById(materialRequestId).orElse(null);
        if (materialRequest == null) {
            return false;
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return false;
        }

        return materialRequest.getRequestedBy().getId().equals(user.getId());
    }
}