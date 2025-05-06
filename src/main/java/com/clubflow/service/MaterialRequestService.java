package com.clubflow.service;

import com.clubflow.dto.mapper.MaterialRequestMapper;
import com.clubflow.dto.request.MaterialRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.MaterialRequest;
import com.clubflow.model.User;
import com.clubflow.repository.MaterialRequestRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialRequestService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialRequestService.class);

    private final MaterialRequestRepository materialRequestRepository;
    private final UserRepository userRepository;
    private final MaterialRequestMapper materialRequestMapper;

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository,
                                  UserRepository userRepository,
                                  MaterialRequestMapper materialRequestMapper) {
        this.materialRequestRepository = materialRequestRepository;
        this.userRepository = userRepository;
        this.materialRequestMapper = materialRequestMapper;
    }

    public List<MaterialRequest> getAllMaterialRequests() {
        return materialRequestRepository.findAll();
    }

    public MaterialRequest getMaterialRequestById(Long id) {
        return materialRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialRequest", "id", id));
    }

    public List<MaterialRequest> getMaterialRequestsByClub(String club) {
        return materialRequestRepository.findByClub(club);
    }

    public List<MaterialRequest> getMaterialRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return materialRequestRepository.findByRequestedBy(user);
    }

    public List<MaterialRequest> getMaterialRequestsByStatus(MaterialRequest.RequestStatus status) {
        return materialRequestRepository.findByStatus(status);
    }

    @Transactional
    public MaterialRequest createMaterialRequest(Long userId, MaterialRequestDto materialRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        MaterialRequest materialRequest = materialRequestMapper.toEntity(materialRequestDto);
        materialRequest.setRequestedBy(user);
        materialRequest.setStatus(MaterialRequest.RequestStatus.PENDING);

        MaterialRequest savedRequest = materialRequestRepository.save(materialRequest);
        logger.info("Material request created successfully for club: {}", materialRequest.getClub());
        return savedRequest;
    }

    @Transactional
    public MaterialRequest updateMaterialRequestStatus(Long id, MaterialRequest.RequestStatus status, String rejectionReason) {
        MaterialRequest materialRequest = materialRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialRequest", "id", id));

        materialRequest.setStatus(status);
        if (status == MaterialRequest.RequestStatus.REJECTED && rejectionReason != null) {
            materialRequest.setRejectionReason(rejectionReason);
        }

        MaterialRequest updatedRequest = materialRequestRepository.save(materialRequest);
        logger.info("Material request status updated to {} for request ID: {}", status, id);
        return updatedRequest;
    }

    @Transactional
    public void deleteMaterialRequest(Long id) {
        MaterialRequest materialRequest = materialRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialRequest", "id", id));
        materialRequestRepository.delete(materialRequest);
        logger.info("Material request deleted successfully: {}", id);
    }
}