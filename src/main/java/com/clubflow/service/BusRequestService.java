package com.clubflow.service;


import com.clubflow.dto.mapper.BusRequestMapper;
import com.clubflow.dto.request.BusRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.BusRequest;
import com.clubflow.model.User;
import com.clubflow.repository.BusRequestRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusRequestService {
    private static final Logger logger = LoggerFactory.getLogger(BusRequestService.class);

    private final BusRequestRepository busRequestRepository;
    private final UserRepository userRepository;
    private final BusRequestMapper busRequestMapper;

    public BusRequestService(BusRequestRepository busRequestRepository,
                             UserRepository userRepository,
                             BusRequestMapper busRequestMapper) {
        this.busRequestRepository = busRequestRepository;
        this.userRepository = userRepository;
        this.busRequestMapper = busRequestMapper;
    }

    public List<BusRequest> getAllBusRequests() {
        return busRequestRepository.findAll();
    }

    public BusRequest getBusRequestById(Long id) {
        return busRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusRequest", "id", id));
    }

    public List<BusRequest> getBusRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return busRequestRepository.findByUser(user);
    }

    public List<BusRequest> getBusRequestsByStatus(BusRequest.RequestStatus status) {
        return busRequestRepository.findByStatus(status);
    }

    @Transactional
    public BusRequest createBusRequest(Long userId, BusRequestDto busRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        BusRequest busRequest = busRequestMapper.toEntity(busRequestDto);
        busRequest.setUser(user);
        busRequest.setStatus(BusRequest.RequestStatus.PENDING);

        BusRequest savedRequest = busRequestRepository.save(busRequest);
        logger.info("Bus request created successfully for user: {}", user.getEmail());
        return savedRequest;
    }

    @Transactional
    public BusRequest updateBusRequestStatus(Long id, BusRequest.RequestStatus status, String rejectionReason) {
        BusRequest busRequest = busRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusRequest", "id", id));

        busRequest.setStatus(status);
        if (status == BusRequest.RequestStatus.REJECTED && rejectionReason != null) {
            busRequest.setRejectionReason(rejectionReason);
        }

        BusRequest updatedRequest = busRequestRepository.save(busRequest);
        logger.info("Bus request status updated to {} for request ID: {}", status, id);
        return updatedRequest;
    }

    @Transactional
    public void deleteBusRequest(Long id) {
        BusRequest busRequest = busRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusRequest", "id", id));
        busRequestRepository.delete(busRequest);
        logger.info("Bus request deleted successfully: {}", id);
    }
}