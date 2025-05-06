package com.clubflow;

import com.clubflow.dto.mapper.RoomRequestMapper;
import com.clubflow.dto.request.RoomRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.RoomRequest;
import com.clubflow.model.User;
import com.clubflow.repository.RoomRequestRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.RoomRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomRequestServiceTest {

    @InjectMocks
    private RoomRequestService roomRequestService;

    @Mock
    private RoomRequestRepository roomRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRequestMapper roomRequestMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoomRequest_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail("test@enicarthage.tn");

        RoomRequestDto dto = new RoomRequestDto();
        dto.setRoomNumber("A101");
        dto.setBuilding("Main");
        dto.setDate("2025-05-10");
        dto.setStartTime("10:00");
        dto.setDuration(60);
        dto.setPurpose("Meeting");

        RoomRequest request = new RoomRequest();
        request.setRoomNumber(dto.getRoomNumber());
        request.setDate(dto.getDate());
        request.setStartTime(dto.getStartTime());
        request.setDuration(dto.getDuration());
        request.setPurpose(dto.getPurpose());

        RoomRequest savedRequest = new RoomRequest();
        savedRequest.setId(1L);
        savedRequest.setUser(user);
        savedRequest.setStatus(RoomRequest.RequestStatus.PENDING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roomRequestRepository.findOverlappingRequests(anyString(), anyString(), anyString(), anyString())).thenReturn(Collections.emptyList());
        when(roomRequestMapper.toEntity(dto)).thenReturn(request);
        when(roomRequestRepository.save(any(RoomRequest.class))).thenReturn(savedRequest);

        RoomRequest result = roomRequestService.createRoomRequest(userId, dto);

        assertNotNull(result);
        assertEquals(RoomRequest.RequestStatus.PENDING, result.getStatus());
        verify(roomRequestRepository).save(any(RoomRequest.class));
    }

    @Test
    void testCreateRoomRequest_RoomNotAvailable() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        RoomRequestDto dto = new RoomRequestDto();
        dto.setRoomNumber("A101");
        dto.setDate("2025-05-10");
        dto.setStartTime("10:00");
        dto.setDuration(60);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roomRequestRepository.findOverlappingRequests(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(List.of(new RoomRequest())); // simulate conflict

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> roomRequestService.createRoomRequest(userId, dto));
        assertEquals("Room is not available at the requested time", ex.getMessage());
    }

    @Test
    void testUpdateRoomRequestStatus_Success() {
        Long requestId = 1L;
        RoomRequest request = new RoomRequest();
        request.setId(requestId);
        request.setStatus(RoomRequest.RequestStatus.PENDING);

        when(roomRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(roomRequestRepository.save(any(RoomRequest.class))).thenReturn(request);

        RoomRequest result = roomRequestService.updateRoomRequestStatus(requestId, RoomRequest.RequestStatus.APPROVED, null);

        assertEquals(RoomRequest.RequestStatus.APPROVED, result.getStatus());
    }

    @Test
    void testDeleteRoomRequest_Success() {
        Long requestId = 1L;
        RoomRequest request = new RoomRequest();
        request.setId(requestId);

        when(roomRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        roomRequestService.deleteRoomRequest(requestId);

        verify(roomRequestRepository).delete(request);
    }

    @Test
    void testGetRoomRequestById_NotFound() {
        when(roomRequestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roomRequestService.getRoomRequestById(999L));
    }

    @Test
    void testIsRoomAvailable_ReturnsTrue() {
        when(roomRequestRepository.findOverlappingRequests(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        boolean result = roomRequestService.isRoomAvailable("B201", "2025-05-15", "14:00", 60);
        assertTrue(result);
    }

    @Test
    void testIsRoomAvailable_ReturnsFalse() {
        when(roomRequestRepository.findOverlappingRequests(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(List.of(new RoomRequest()));

        boolean result = roomRequestService.isRoomAvailable("B201", "2025-05-15", "14:00", 60);
        assertFalse(result);
    }
}
