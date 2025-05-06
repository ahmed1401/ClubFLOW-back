package com.clubflow.controller;

import com.clubflow.dto.request.ComplaintDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.Complaint;
import com.clubflow.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/complaints")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Complaints", description = "Complaint management API")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all complaints (Admin only)")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @complaintSecurity.isOwner(#id, principal)")
    @Operation(summary = "Get complaint by ID (Admin or owner)")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping("/club/{club}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get complaints by club (Admin only)")
    public ResponseEntity<List<Complaint>> getComplaintsByClub(@PathVariable String club) {
        List<Complaint> complaints = complaintService.getComplaintsByClub(club);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/subject/{subject}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get complaints by subject (Admin only)")
    public ResponseEntity<List<Complaint>> getComplaintsBySubject(@PathVariable String subject) {
        List<Complaint> complaints = complaintService.getComplaintsBySubject(subject);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    @Operation(summary = "Get complaints by user (Admin or owner)")
    public ResponseEntity<List<Complaint>> getComplaintsByUser(@PathVariable Long userId) {
        List<Complaint> complaints = complaintService.getComplaintsByUser(userId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get complaints by status (Admin only)")
    public ResponseEntity<List<Complaint>> getComplaintsByStatus(@PathVariable String status) {
        Complaint.ComplaintStatus complaintStatus = Complaint.ComplaintStatus.valueOf(status.toUpperCase());
        List<Complaint> complaints = complaintService.getComplaintsByStatus(complaintStatus);
        return ResponseEntity.ok(complaints);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new complaint")
    public ResponseEntity<ApiResponse> createComplaint(
            @PathVariable Long userId,
            @Valid @RequestBody ComplaintDto complaintDto) {
        Complaint complaint = complaintService.createComplaint(userId, complaintDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Complaint created successfully", complaint));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update complaint status (Admin only)")
    public ResponseEntity<ApiResponse> updateComplaintStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        String response = statusUpdate.get("response");

        Complaint.ComplaintStatus complaintStatus = Complaint.ComplaintStatus.valueOf(status.toUpperCase());
        Complaint complaint = complaintService.updateComplaintStatus(id, complaintStatus, response);

        return ResponseEntity.ok(new ApiResponse(true, "Complaint status updated successfully", complaint));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete complaint (Admin only)")
    public ResponseEntity<ApiResponse> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok(new ApiResponse(true, "Complaint deleted successfully"));
    }
}
