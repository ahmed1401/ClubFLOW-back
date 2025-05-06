package com.clubflow;

import com.clubflow.dto.mapper.WorkshopRequestMapper;
import com.clubflow.dto.request.WorkshopRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Member;
import com.clubflow.model.WorkshopRequest;
import com.clubflow.model.WorkshopRequest.RequestStatus;
import com.clubflow.repository.MemberRepository;
import com.clubflow.repository.WorkshopRequestRepository;
import com.clubflow.service.WorkshopRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkshopRequestServiceTest {

    @Mock
    private WorkshopRequestRepository workshopRequestRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WorkshopRequestMapper workshopRequestMapper;

    @InjectMocks
    private WorkshopRequestService workshopRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWorkshopRequests() {
        List<WorkshopRequest> mockList = List.of(new WorkshopRequest(), new WorkshopRequest());
        when(workshopRequestRepository.findAll()).thenReturn(mockList);

        List<WorkshopRequest> result = workshopRequestService.getAllWorkshopRequests();
        assertEquals(2, result.size());
        verify(workshopRequestRepository).findAll();
    }

    @Test
    void testGetWorkshopRequestById_Success() {
        WorkshopRequest request = new WorkshopRequest();
        request.setId(1L);
        when(workshopRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        WorkshopRequest result = workshopRequestService.getWorkshopRequestById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetWorkshopRequestById_NotFound() {
        when(workshopRequestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> workshopRequestService.getWorkshopRequestById(1L));
    }

    @Test
    void testGetWorkshopRequestsByMember_Success() {
        Member member = new Member();
        member.setId(1L);
        List<WorkshopRequest> mockList = List.of(new WorkshopRequest());
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(workshopRequestRepository.findByMember(member)).thenReturn(mockList);

        List<WorkshopRequest> result = workshopRequestService.getWorkshopRequestsByMember(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateWorkshopRequest_WithValidMember() {
        WorkshopRequestDto dto = new WorkshopRequestDto();
        dto.setTitle("New Workshop");
        dto.setReason("Skill improvement");
        dto.setMemberId(1L);

        Member member = new Member();
        member.setId(1L);

        WorkshopRequest entity = new WorkshopRequest();
        entity.setTitle(dto.getTitle());
        entity.setReason(dto.getReason());
        entity.setMember(member);
        entity.setStatus(RequestStatus.PENDING);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(workshopRequestMapper.toEntity(dto)).thenReturn(entity);
        when(workshopRequestRepository.save(any(WorkshopRequest.class))).thenReturn(entity);

        WorkshopRequest result = workshopRequestService.createWorkshopRequest(dto);
        assertEquals(RequestStatus.PENDING, result.getStatus());
        verify(workshopRequestRepository).save(entity);
    }

    @Test
    void testUpdateWorkshopRequestStatus() {
        WorkshopRequest request = new WorkshopRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.PENDING);

        when(workshopRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(workshopRequestRepository.save(any(WorkshopRequest.class))).thenReturn(request);

        WorkshopRequest updated = workshopRequestService.updateWorkshopRequestStatus(1L, RequestStatus.APPROVED, null);
        assertEquals(RequestStatus.APPROVED, updated.getStatus());
    }

    @Test
    void testDeleteWorkshopRequest_Success() {
        WorkshopRequest request = new WorkshopRequest();
        request.setId(1L);
        when(workshopRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        workshopRequestService.deleteWorkshopRequest(1L);
        verify(workshopRequestRepository).delete(request);
    }
}
