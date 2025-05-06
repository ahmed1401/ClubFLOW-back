package com.clubflow.security;

import com.clubflow.model.Complaint;
import com.clubflow.model.User;
import com.clubflow.repository.ComplaintRepository;
import com.clubflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("complaintSecurity")
public class ComplaintSecurity {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintSecurity(ComplaintRepository complaintRepository, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long complaintId, UserDetails userDetails) {
        Complaint complaint = complaintRepository.findById(complaintId).orElse(null);
        if (complaint == null) {
            return false;
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return false;
        }

        return complaint.getSubmittedBy().getId().equals(user.getId());
    }
}