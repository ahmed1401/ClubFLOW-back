package com.clubflow.security;


import com.clubflow.model.RoomRequest;
import com.clubflow.model.User;
import com.clubflow.repository.RoomRequestRepository;
import com.clubflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("roomRequestSecurity")
public class RoomRequestSecurity {

    private final RoomRequestRepository roomRequestRepository;
    private final UserRepository userRepository;

    public RoomRequestSecurity(RoomRequestRepository roomRequestRepository, UserRepository userRepository) {
        this.roomRequestRepository = roomRequestRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long roomRequestId, UserDetails userDetails) {
        RoomRequest roomRequest = roomRequestRepository.findById(roomRequestId).orElse(null);
        if (roomRequest == null) {
            return false;
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return false;
        }

        return roomRequest.getUser().getId().equals(user.getId());
    }
}