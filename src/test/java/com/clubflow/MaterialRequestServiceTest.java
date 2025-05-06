package com.clubflow;

import com.clubflow.dto.mapper.MaterialRequestMapper;
import com.clubflow.dto.request.MaterialRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.MaterialRequest;
import com.clubflow.model.MaterialRequest.RequestStatus;
import com.clubflow.model.User;
import com.clubflow.repository.MaterialRequestRepository;
import com.clubflow.repository.UserRepository;

import com.clubflow.service.MaterialRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialRequestServiceTest {

    private MaterialRequestRepository materialRequestRepository;
    private UserRepository userRepository;
    private MaterialRequestMapper materialRequestMapper;
    private MaterialRequestService materialRequestService;

    @BeforeEach
    void setUp() {
        materialRequestRepository = mock(MaterialRequestRepository.class);
        userRepository = mock(UserRepository.class);
        materialRequestMapper = mock(MaterialRequestMapper.class);
        materialRequestService = new MaterialRequestService(
                materialRequestRepository, userRepository, materialRequestMapper
        );
    }



    @Test
    void testGetMaterialRequestById_found() {
        MaterialRequest request = new MaterialRequest();
        request.setId(100L);

        when(materialRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        MaterialRequest result = materialRequestService.getMaterialRequestById(100L);

        assertEquals(100L, result.getId());
    }

    @Test
    void testGetMaterialRequestById_notFound() {
        when(materialRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> materialRequestService.getMaterialRequestById(99L));
    }

    @Test
    void testUpdateMaterialRequestStatus_toApproved() {
        MaterialRequest request = new MaterialRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.PENDING);

        when(materialRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(materialRequestRepository.save(any(MaterialRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        MaterialRequest result = materialRequestService.updateMaterialRequestStatus(1L, RequestStatus.APPROVED, null);

        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertNull(result.getRejectionReason());
    }

    @Test
    void testUpdateMaterialRequestStatus_toRejectedWithReason() {
        MaterialRequest request = new MaterialRequest();
        request.setId(2L);

        when(materialRequestRepository.findById(2L)).thenReturn(Optional.of(request));
        when(materialRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MaterialRequest result = materialRequestService.updateMaterialRequestStatus(2L, RequestStatus.REJECTED, "Not enough resources");

        assertEquals(RequestStatus.REJECTED, result.getStatus());
        assertEquals("Not enough resources", result.getRejectionReason());
    }

    @Test
    void testDeleteMaterialRequest_success() {
        MaterialRequest request = new MaterialRequest();
        request.setId(5L);

        when(materialRequestRepository.findById(5L)).thenReturn(Optional.of(request));

        materialRequestService.deleteMaterialRequest(5L);

        verify(materialRequestRepository).delete(request);
    }

    @Test
    void testGetMaterialRequestsByUser_success() {
        Long userId = 3L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        materialRequestService.getMaterialRequestsByUser(userId);

        verify(materialRequestRepository).findByRequestedBy(user);
    }

    @Test
    void testGetMaterialRequestsByClub_success() {
        String clubName = "MediaClub";

        materialRequestService.getMaterialRequestsByClub(clubName);

        verify(materialRequestRepository).findByClub(clubName);
    }

    @Test
    void testGetMaterialRequestsByStatus_success() {
        materialRequestService.getMaterialRequestsByStatus(RequestStatus.APPROVED);

        verify(materialRequestRepository).findByStatus(RequestStatus.APPROVED);
    }

    @Test
    void testGetAllMaterialRequests_success() {
        materialRequestService.getAllMaterialRequests();

        verify(materialRequestRepository).findAll();
    }
}
