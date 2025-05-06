package com.clubflow;

import com.clubflow.dto.mapper.ComplaintMapper;
import com.clubflow.dto.request.ComplaintDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Complaint;
import com.clubflow.model.User;
import com.clubflow.repository.ComplaintRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ComplaintMapper complaintMapper;

    @InjectMocks
    private ComplaintService complaintService;

    private User user;
    private ComplaintDto complaintDto;
    private Complaint complaint;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock User
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // Create ComplaintDto for tests
        complaintDto = new ComplaintDto();
        complaintDto.setClub("Football Club");
        complaintDto.setSubject("Loud Noise");
        complaintDto.setMessage("The noise during the night is disturbing.");
        complaintDto.setDate(String.valueOf(LocalDate.of(2025, 6, 1)));

        // Create Complaint for tests
        complaint = new Complaint();
        complaint.setId(1L);
        complaint.setClub("Football Club");
        complaint.setSubject("Loud Noise");
        complaint.setMessage("The noise during the night is disturbing.");
        complaint.setDate(LocalDate.of(2025, 6, 1));
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);
    }

    // Unit Test for Complaint Model
    @Test
    public void testComplaintCreation() {
        Complaint complaint = new Complaint();
        complaint.setClub("Football Club");
        complaint.setSubject("Loud Noise");
        complaint.setMessage("The noise during the night is disturbing.");
        complaint.setDate(LocalDate.of(2025, 6, 1));
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);

        assertEquals("Football Club", complaint.getClub());
        assertEquals("Loud Noise", complaint.getSubject());
        assertEquals(Complaint.ComplaintStatus.PENDING, complaint.getStatus());
    }

    @Test
    public void testComplaintEnumStatus() {
        Complaint.ComplaintStatus status = Complaint.ComplaintStatus.valueOf("RESOLVED");
        assertNotNull(status);
        assertEquals("RESOLVED", status.name());
    }



    @Test
    public void testGetComplaintById() {
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        Complaint foundComplaint = complaintService.getComplaintById(1L);

        assertEquals(1L, foundComplaint.getId());
    }





    @Test
    public void testDeleteComplaint() {
        Complaint complaintToDelete = new Complaint();
        complaintToDelete.setId(1L);

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaintToDelete));

        complaintService.deleteComplaint(1L);

        verify(complaintRepository, times(1)).delete(complaintToDelete);
    }

    @Test
    public void testGetComplaintsByClub() {
        when(complaintRepository.findByClub("Football Club")).thenReturn(List.of(complaint));

        List<Complaint> complaints = complaintService.getComplaintsByClub("Football Club");

        assertFalse(complaints.isEmpty());
        assertEquals("Football Club", complaints.get(0).getClub());
    }

    @Test
    public void testGetComplaintsBySubject() {
        when(complaintRepository.findBySubject("Loud Noise")).thenReturn(List.of(complaint));

        List<Complaint> complaints = complaintService.getComplaintsBySubject("Loud Noise");

        assertFalse(complaints.isEmpty());
        assertEquals("Loud Noise", complaints.get(0).getSubject());
    }



    @Test
    public void testGetComplaintsByStatus() {
        when(complaintRepository.findByStatus(Complaint.ComplaintStatus.PENDING)).thenReturn(List.of(complaint));

        List<Complaint> complaints = complaintService.getComplaintsByStatus(Complaint.ComplaintStatus.PENDING);

        assertFalse(complaints.isEmpty());
        assertEquals(Complaint.ComplaintStatus.PENDING, complaints.get(0).getStatus());
    }
}
