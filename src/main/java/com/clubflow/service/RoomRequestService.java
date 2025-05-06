package com.clubflow.service;


import com.clubflow.dto.mapper.RoomRequestMapper;
import com.clubflow.dto.request.RoomRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.RoomRequest;
import com.clubflow.model.User;
import com.clubflow.repository.RoomRequestRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RoomRequestService {
    private static final Logger logger = LoggerFactory.getLogger(RoomRequestService.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final RoomRequestRepository roomRequestRepository;
    private final UserRepository userRepository;
    private final RoomRequestMapper roomRequestMapper;

    public RoomRequestService(RoomRequestRepository roomRequestRepository,
                              UserRepository userRepository,
                              RoomRequestMapper roomRequestMapper) {
        this.roomRequestRepository = roomRequestRepository;
        this.userRepository = userRepository;
        this.roomRequestMapper = roomRequestMapper;
    }

    public List<RoomRequest> getAllRoomRequests() {
        return roomRequestRepository.findAll();
    }

    public RoomRequest getRoomRequestById(Long id) {
        return roomRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomRequest", "id", id));
    }

    public List<RoomRequest> getRoomRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return roomRequestRepository.findByUser(user);
    }

    public List<RoomRequest> getRoomRequestsByStatus(RoomRequest.RequestStatus status) {
        return roomRequestRepository.findByStatus(status);
    }

    public boolean isRoomAvailable(String roomNumber, String date, String startTime, Integer duration) {
        LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime end = start.plusMinutes(duration);

        List<RoomRequest> overlappingRequests = roomRequestRepository.findOverlappingRequests(
                roomNumber, date, startTime, end.format(TIME_FORMATTER));

        return overlappingRequests.isEmpty();
    }

    @Transactional
    public RoomRequest createRoomRequest(Long userId, RoomRequestDto roomRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Check if room is available
        if (!isRoomAvailable(roomRequestDto.getRoomNumber(), roomRequestDto.getDate(),
                roomRequestDto.getStartTime(), roomRequestDto.getDuration())) {
            logger.error("Room {} is not available at the requested time", roomRequestDto.getRoomNumber());
            throw new RuntimeException("Room is not available at the requested time");
        }

        RoomRequest roomRequest = roomRequestMapper.toEntity(roomRequestDto);
        roomRequest.setUser(user);
        roomRequest.setStatus(RoomRequest.RequestStatus.PENDING);

        RoomRequest savedRequest = roomRequestRepository.save(roomRequest);
        logger.info("Room request created successfully for user: {}", user.getEmail());
        return savedRequest;
    }

    @Transactional
    public RoomRequest updateRoomRequestStatus(Long id, RoomRequest.RequestStatus status, String rejectionReason) {
        RoomRequest roomRequest = roomRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomRequest", "id", id));

        roomRequest.setStatus(status);
        if (status == RoomRequest.RequestStatus.REJECTED && rejectionReason != null) {
            roomRequest.setRejectionReason(rejectionReason);
        }

        RoomRequest updatedRequest = roomRequestRepository.save(roomRequest);
        logger.info("Room request status updated to {} for request ID: {}", status, id);
        return updatedRequest;
    }

    @Transactional
    public void deleteRoomRequest(Long id) {
        RoomRequest roomRequest = roomRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomRequest", "id", id));
        roomRequestRepository.delete(roomRequest);
        logger.info("Room request deleted successfully: {}", id);
    }
}