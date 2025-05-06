package com.clubflow.controller;


import com.clubflow.dto.request.WorkshopRequestDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.WorkshopRequest;
import com.clubflow.service.WorkshopRequestService;
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
@RequestMapping("/workshop-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Workshop Requests", description = "Workshop request management API")
public class WorkshopRequestController {

    private final WorkshopRequestService workshopRequestService;

    public WorkshopRequestController(WorkshopRequestService workshopRequestService) {
        this.workshopRequestService = workshopRequestService;
    }

    @GetMapping
    @Operation(summary = "Get all workshop requests")
    public ResponseEntity<List<WorkshopRequest>> getAllWorkshopRequests() {
        List<WorkshopRequest> workshopRequests = workshopRequestService.getAllWorkshopRequests();
        return ResponseEntity.ok(workshopRequests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workshop request by ID")
    public ResponseEntity<WorkshopRequest> getWorkshopRequestById(@PathVariable Long id) {
        WorkshopRequest workshopRequest = workshopRequestService.getWorkshopRequestById(id);
        return ResponseEntity.ok(workshopRequest);
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get workshop requests by member")
    public ResponseEntity<List<WorkshopRequest>> getWorkshopRequestsByMember(@PathVariable Long memberId) {
        List<WorkshopRequest> workshopRequests = workshopRequestService.getWorkshopRequestsByMember(memberId);
        return ResponseEntity.ok(workshopRequests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get workshop requests by status (Admin only)")
    public ResponseEntity<List<WorkshopRequest>> getWorkshopRequestsByStatus(@PathVariable String status) {
        WorkshopRequest.RequestStatus requestStatus = WorkshopRequest.RequestStatus.valueOf(status.toUpperCase());
        List<WorkshopRequest> workshopRequests = workshopRequestService.getWorkshopRequestsByStatus(requestStatus);
        return ResponseEntity.ok(workshopRequests);
    }

    @PostMapping
    @Operation(summary = "Create a new workshop request")
    public ResponseEntity<ApiResponse> createWorkshopRequest(@Valid @RequestBody WorkshopRequestDto workshopRequestDto) {
        WorkshopRequest workshopRequest = workshopRequestService.createWorkshopRequest(workshopRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Workshop request created successfully", workshopRequest));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update workshop request status (Admin only)")
    public ResponseEntity<ApiResponse> updateWorkshopRequestStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        String rejectionReason = statusUpdate.get("rejectionReason");

        WorkshopRequest.RequestStatus requestStatus = WorkshopRequest.RequestStatus.valueOf(status.toUpperCase());
        WorkshopRequest workshopRequest = workshopRequestService.updateWorkshopRequestStatus(id, requestStatus, rejectionReason);

        return ResponseEntity.ok(new ApiResponse(true, "Workshop request status updated successfully", workshopRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete workshop request (Admin only)")
    public ResponseEntity<ApiResponse> deleteWorkshopRequest(@PathVariable Long id) {
        workshopRequestService.deleteWorkshopRequest(id);
        return ResponseEntity.ok(new ApiResponse(true, "Workshop request deleted successfully"));
    }
}