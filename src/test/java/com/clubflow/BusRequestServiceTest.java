package com.clubflow;

import com.clubflow.dto.mapper.BusRequestMapper;
import com.clubflow.dto.request.BusRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.BusRequest;
import com.clubflow.model.User;
import com.clubflow.repository.BusRequestRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.BusRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class BusRequestServiceTest {

    @Mock
    private BusRequestRepository busRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BusRequestMapper busRequestMapper;

    @InjectMocks
    private BusRequestService busRequestService;

    private User user;
    private BusRequestDto busRequestDto;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create mock User
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // Create BusRequestDto for tests
        busRequestDto = new BusRequestDto();
        busRequestDto.setDestination("New York");
        busRequestDto.setDepartureDate("2025-06-01");
        busRequestDto.setNumberOfPassengers(4);
        busRequestDto.setPurpose("Business");
    }

    // Unit Test for BusRequest Model
    @Test
    public void testBusRequestCreation() {
        BusRequest busRequest = new BusRequest();
        busRequest.setDestination("New York");
        busRequest.setDepartureDate("2025-06-01");
        busRequest.setNumberOfPassengers(4);
        busRequest.setPurpose("Business");
        busRequest.setStatus(BusRequest.RequestStatus.PENDING);

        assertEquals("New York", busRequest.getDestination());
        assertEquals(4, busRequest.getNumberOfPassengers());
        assertEquals(BusRequest.RequestStatus.PENDING, busRequest.getStatus());
    }

    @Test
    public void testEnumStatus() {
        BusRequest.RequestStatus status = BusRequest.RequestStatus.valueOf("APPROVED");
        assertNotNull(status);
        assertEquals("APPROVED", status.name());
    }



    @Test
    public void testGetBusRequestById() {
        BusRequest busRequest = new BusRequest();
        busRequest.setId(1L);

        when(busRequestRepository.findById(1L)).thenReturn(Optional.of(busRequest));

        BusRequest foundBusRequest = busRequestService.getBusRequestById(1L);

        assertEquals(1L, foundBusRequest.getId());
    }





    @Test
    public void testDeleteBusRequest() {
        BusRequest busRequest = new BusRequest();
        busRequest.setId(1L);

        when(busRequestRepository.findById(1L)).thenReturn(Optional.of(busRequest));

        busRequestService.deleteBusRequest(1L);

        verify(busRequestRepository, times(1)).delete(busRequest);
    }
}
